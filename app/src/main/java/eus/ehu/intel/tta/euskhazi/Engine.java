package eus.ehu.intel.tta.euskhazi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.gson.Gson;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;

import eus.ehu.intel.tta.euskhazi.services.PreferencesManager;
import eus.ehu.intel.tta.euskhazi.services.dataType.Mobile;
import eus.ehu.intel.tta.euskhazi.services.dataType.User;

/**
 * Created by eduardo.zarate on 18/12/2015.
 */
public class Engine {
    private static String TAG = Engine.class.getCanonicalName();
    private static Engine mEngine=null;
    private Context contextApplication=null;
    private PreferencesManager mPreferencesManager;
    private Mobile mMobile;
    private User mUserNow;


    private static final String PREFERENCE_MOBILE_ID=TAG+"PREFERENCE_MOBILE_ID";


    //Eventos
    private OnUserListener onUserListener=null;


    public Engine(Context contextApplication){
        this.contextApplication=contextApplication;
        this.mPreferencesManager=PreferencesManager.getInstance();
        updateMobile();

    }



    public static Engine getInstance(Context contextApplication){
        if(contextApplication==null){
            Log.e(TAG,"The parameter contextApplication is nullpoint");
            return null;
        }
        if(mEngine == null){
            Log.d(TAG,"the instance engine is created");
            mEngine = new Engine(contextApplication);
        }
        return mEngine;
    }


    //INIT USERS//

    public boolean saveUser(){
        if(mMobile!=null){
            if(saveMobile()){
                if(onUserListener!=null) onUserListener.onSaveUser(true);
                return true;
            }

        }
        if(onUserListener!=null) onUserListener.onSaveUser(false);
        return false;
    }


    public boolean addUser(User user) throws ExcepcionUser {
        if(user==null || user.getName()==null || user.getPass()==null || user.getName().equals("") || user.getPass().equals("")){
            throw new ExcepcionUser(new Integer(ExcepcionUser.PARAMETERS_NULL).toString());
        }
        if(!isUser(user)){
            if(mMobile!=null){
                mMobile.getUsers().add(user);
                boolean result=saveMobile();
                if(onUserListener!=null) onUserListener.onAddUser(result);
                return result;
            }
        }
        if(onUserListener!=null) onUserListener.onAddUser(false);
        return false;
    }

    public boolean getUser(User user) throws ExcepcionUser {
        if(user==null){
            throw new ExcepcionUser(new Integer(ExcepcionUser.PARAMETERS_NULL).toString());
        }
        if(mMobile==null){
            List<User> users=mMobile.getUsers();
            for(int con=0;con<users.size();con++){
                User userFor=users.get(con);
                if(userFor.getName().equals(user.getName()) && userFor.getPass().equals(user.getPass())){
                    if(onUserListener!=null) onUserListener.onGetUser(userFor);
                    return true;
                }
            }
        }
        if(onUserListener!=null)onUserListener.onGetUser(null);
        return false;
    }
    public boolean getUser(String name,String password) throws ExcepcionUser {
        if(name==null || password==null || name.equals("") || password.equals("")) {
            throw new ExcepcionUser(new Integer(ExcepcionUser.PARAMETERS_NULL).toString());
        }

        User user=new User();
        user.setName(name);
        user.setPass(password);
        return getUser(user);
    }

    public boolean getUserNow(){
        if(mUserNow!=null){
            if(onUserListener!=null)onUserListener.onGetUserNow(mUserNow);
        }else{
            return false;
        }
        return true;
    }

    public boolean isUser(User user) throws ExcepcionUser {
        if(user==null){
            throw new ExcepcionUser(new Integer(ExcepcionUser.PARAMETERS_NULL).toString());
        }
        if(mMobile!=null){
            List<User> users=mMobile.getUsers();
            for(int con=0;con<users.size();con++){
                User userFor=users.get(con);
                if(userFor.getName().equals(user.getName()) && userFor.getPass().equals(user.getPass())){
                    if(onUserListener!=null)onUserListener.onIsUser(true);
                    return true;
                }
            }
        }
        if(onUserListener!=null)onUserListener.onIsUser(false);
        return false;
    }


    public boolean isUser(String name,String password) throws ExcepcionUser {
        if(name==null || password==null || name.equals("") || password.equals("")) {
            throw new ExcepcionUser(new Integer(ExcepcionUser.PARAMETERS_NULL).toString());
        }
        User user=new User();
        user.setName(name);
        user.setPass(password);
        return isUser(user);
    }


    public interface OnUserListener{
        public void onGetUser(User user);
        public void onGetUserNow(User user);
        public void onIsUser(boolean isUser);
        public void onAddUser(boolean isAddUser);
        public void onSaveUser(boolean isSaveUser);
    }

    public void setOnUserListener(OnUserListener onUserListener){
        this.onUserListener=onUserListener;
    }

    public class ExcepcionUser extends Exception {
        public static final int PARAMETERS_NULL=1;
        public ExcepcionUser(String msg) {
            super(msg);
        }
    }

    //END USERS//

    //MOBILE INIT//

    private boolean saveMobile(){
        //Generamos el json
        Gson gson=new Gson();
        String json=gson.toJson(mMobile);
        return mPreferencesManager.putString(contextApplication, PREFERENCE_MOBILE_ID, json);
    }

    private boolean updateMobile(){
        String json=mPreferencesManager.getString(contextApplication, PREFERENCE_MOBILE_ID);
        if(!json.equals(PreferencesManager.STRING_DEFAULT) && json!=null){
            Gson gson=new Gson();
            gson.fromJson(json,Mobile.class);
        }else{
            //No hay movil guardado con lo que hay que crearlo
            mMobile=new Mobile();
            mMobile.setMobilesMAC(getMACaddress());
            mMobile.setUsers(new ArrayList<User>());
        }
        return false;
    }



    //MOBILE END//


    private String getMACaddress(){
        WifiManager manager = (WifiManager) contextApplication.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }



}
