package eus.ehu.intel.tta.euskhazi.screen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import eus.ehu.intel.tta.euskhazi.R;
import eus.ehu.intel.tta.euskhazi.services.AudioPlayer;
import eus.ehu.intel.tta.euskhazi.services.LevelsManager;
import eus.ehu.intel.tta.euskhazi.services.dataType.Exam;
import eus.ehu.intel.tta.euskhazi.services.dataType.exam.Level;
import eus.ehu.intel.tta.euskhazi.services.dataType.exam.entzunezkoa.Entzunezkoa;
import eus.ehu.intel.tta.euskhazi.services.dataType.exam.entzunezkoa.StatementEntzunezkoa;
import eus.ehu.intel.tta.euskhazi.services.dataType.exam.sinonimoak.StatementSinonimoak;

public class ScreenEntzumena extends ScreenBase {

    private Uri uri;
    private MediaPlayer mediaPlayer;
    public AudioPlayer audioPlayer;
    private LinearLayout entzumena_LinearLayout_audioplay;
    private View vv_play_video_dialog_detail;
    private Button entzumena_play_button;
    public Entzunezkoa entzunezkoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_entzumena);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        entzumena_play_button=(Button)findViewById(R.id.entzumena_play_button);

        vv_play_video_dialog_detail=findViewById(R.id.vv_play_video_dialog_detail);




        entzumena_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(vv_play_video_dialog_detail,entzunezkoa.getAudioUrl());
            }
        });


        Intent intent = getIntent();
        final int numeroExamen = intent.getExtras().getInt("numeroExamen");
        final String levelString = intent.getStringExtra("level");

        TextView textLogin = (TextView)findViewById(R.id.entzumena_title_textView);
        textLogin.setText("Entzumena " + (numeroExamen + 1) + " - " + levelString);

        entzumena_LinearLayout_audioplay = (LinearLayout)findViewById(R.id.entzumena_LinearLayout_audioplay);

        mEngine.setOnGetLevelListener(new LevelsManager.OnGetLevelListener() {


            @Override
            public void onGetLevel(Level levels) {
                if (levels == null || levels.getEntzunezkoas() == null) {
                    Toast.makeText(getApplicationContext(), R.string.load_exam_incorrectly, Toast.LENGTH_SHORT).show();
                    return;
                }


                /*
                //String audioUrl = levels.getEntzunezkoas().get(numeroExamen).getAudioUrl();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lentillak);
                mediaPlayer.start();
                */
                entzunezkoa=levels.getEntzunezkoas().get(numeroExamen);
                final ArrayList<StatementEntzunezkoa> statements = levels.getEntzunezkoas().get(numeroExamen).getStatements();

                TextView statementTextView0 = (TextView) findViewById(R.id.entzumena_statement_textView_0);
                statementTextView0.setText(statements.get(0).getStatement());
                TextView statementTextView1 = (TextView) findViewById(R.id.entzumena_statement_textView_1);
                statementTextView1.setText(statements.get(1).getStatement());
                TextView statementTextView2 = (TextView) findViewById(R.id.entzumena_statement_textView_2);
                statementTextView2.setText(statements.get(2).getStatement());
                TextView statementTextView3 = (TextView) findViewById(R.id.entzumena_statement_textView_3);
                statementTextView3.setText(statements.get(3).getStatement());
                TextView statementTextView4 = (TextView) findViewById(R.id.entzumena_statement_textView_4);
                statementTextView4.setText(statements.get(4).getStatement());

                ArrayList<String> posibleAnswers0 = getPosibleAnswers(statements, 0);
                final RadioGroup radioGroup0 = (RadioGroup) findViewById(R.id.entzumena_radioGroup_0);
                populateRadioGroup(radioGroup0, posibleAnswers0);
                ArrayList<String> posibleAnswers1 = getPosibleAnswers(statements, 1);
                final RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.entzumena_radioGroup_1);
                populateRadioGroup(radioGroup1, posibleAnswers1);
                ArrayList<String> posibleAnswers2 = getPosibleAnswers(statements, 2);
                final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.entzumena_radioGroup_2);
                populateRadioGroup(radioGroup2, posibleAnswers2);
                ArrayList<String> posibleAnswers3 = getPosibleAnswers(statements, 3);
                final RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.entzumena_radioGroup_3);
                populateRadioGroup(radioGroup3, posibleAnswers3);
                ArrayList<String> posibleAnswers4 = getPosibleAnswers(statements, 4);
                final RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.entzumena_radioGroup_4);
                populateRadioGroup(radioGroup4, posibleAnswers4);

                Button zuzenduButton = (Button) findViewById(R.id.entzumena_correct_button);
                zuzenduButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double zuzenak = 0;

                        int answer0 = radioGroup0.getCheckedRadioButtonId();
                        RadioButton radioButton0 = (RadioButton) radioGroup0.getChildAt(Integer.parseInt(statements.get(0).getSolution()));
                        if (radioButton0.getId() == answer0) zuzenak++;
                        int answer1 = radioGroup1.getCheckedRadioButtonId();
                        RadioButton radioButton1 = (RadioButton) radioGroup1.getChildAt(Integer.parseInt(statements.get(1).getSolution()));
                        if (radioButton1.getId() == answer1) zuzenak++;
                        int answer2 = radioGroup2.getCheckedRadioButtonId();
                        RadioButton radioButton2 = (RadioButton) radioGroup2.getChildAt(Integer.parseInt(statements.get(2).getSolution()));
                        if (radioButton2.getId() == answer2) zuzenak++;
                        int answer3 = radioGroup3.getCheckedRadioButtonId();
                        RadioButton radioButton3 = (RadioButton) radioGroup3.getChildAt(Integer.parseInt(statements.get(3).getSolution()));
                        if (radioButton3.getId() == answer3) zuzenak++;
                        int answer4 = radioGroup4.getCheckedRadioButtonId();
                        RadioButton radioButton4 = (RadioButton) radioGroup4.getChildAt(Integer.parseInt(statements.get(4).getSolution()));
                        if (radioButton4.getId() == answer4) zuzenak++;

                        if (zuzenak > 2) {
                            Toast.makeText(getApplicationContext(), R.string.examen_aprobado, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.examen_suspendido, Toast.LENGTH_SHORT).show();
                        }

                        Exam exam = new Exam();
                        exam.setTypeExam("entzumena");
                        exam.setNumExams(numeroExamen);
                        exam.setLevel(levelString);
                        exam.setResult((2 * zuzenak));

                        saveUserExam(exam);
                    }
                });
                mEngine.setOnGetLevelListener(null);
            }
        });
        mEngine.getLevel(levelString);
    }



    private ArrayList<String> getPosibleAnswers(ArrayList<StatementEntzunezkoa> statements, int numeroPregunta){
        ArrayList<String> posibleAnswers = new ArrayList<>();
        posibleAnswers.add(statements.get(numeroPregunta).getAnswers().get(0).getFirst());
        posibleAnswers.add(statements.get(numeroPregunta).getAnswers().get(0).getSecond());
        posibleAnswers.add(statements.get(numeroPregunta).getAnswers().get(0).getThird());

        return posibleAnswers;
    }

    private void populateRadioGroup(RadioGroup radioGroup, ArrayList<String> posibleAnswers){
        //for (String posibleAnswer : posibleAnswers) {
        for (int n =0; n < posibleAnswers.size(); n++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(posibleAnswers.get(n));
            //radioButton.setOnClickListener(this);
            radioGroup.addView(radioButton);

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
