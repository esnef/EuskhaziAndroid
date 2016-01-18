package eus.ehu.intel.tta.euskhazi.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eus.ehu.intel.tta.euskhazi.R;
import eus.ehu.intel.tta.euskhazi.services.LevelsManager;
import eus.ehu.intel.tta.euskhazi.services.dataType.exam.Level;

public class ScreenListaExams extends ScreenBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_exams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String levelString = intent.getStringExtra("level");
        final int typeLevel = intent.getExtras().getInt("typeLevel");
        final TextView tituloTextView = (TextView) findViewById(R.id.content_lista_exams_title_textView);


        mEngine.setOnGetLevelListener(new LevelsManager.OnGetLevelListener() {
            @Override
            public void onGetLevel(Level levels) {
                if (levels == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_hay_examenes), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int cantidadExamenes = 0;
                    switch (typeLevel) {
                        case 0:
                            tituloTextView.setText("Atarikoa " + levelString);
                            cantidadExamenes = levels.getAtarikoas().size();
                            break;
                        case 1:
                            tituloTextView.setText("Idatzizkoa " + levelString);
                            cantidadExamenes = levels.getIdatzizkoas().size();
                            break;
                        case 2:
                            tituloTextView.setText("Sinonimoak " + levelString);
                            cantidadExamenes = levels.getSinonimoaks().size();
                            break;
                        case 3:
                            tituloTextView.setText("Berridazketak " + levelString);
                            cantidadExamenes = levels.getBerridazketaks().size();
                            break;
                        case 4:
                            tituloTextView.setText("Entzunezkoa " + levelString);
                            cantidadExamenes = levels.getEntzunezkoas().size();
                            break;
                        case 5:
                            tituloTextView.setText("Ahozkoa " + levelString);
                            cantidadExamenes = levels.getAhozkoas().size();
                            break;
                    }

                    ArrayList<String> exams = new ArrayList<>();
                    for (int i = 0; i < cantidadExamenes; i++) {
                        String azterketa = "Azterketa " + (i+1);
                        //Button button = new Button(this);
                        //button.setText("Azterketa " + i);
                        exams.add(azterketa);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.textview_layout, R.id.exams_textView, exams);
                    ListView lstOpciones = (ListView) findViewById(R.id.atarikoa_exams_listView);
                    lstOpciones.setAdapter(adapter);

                    lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            Context context = getApplicationContext();
                            Toast.makeText(context, "Aukeratutako azterketa " + (position+1), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                mEngine.setOnGetLevelListener(null);
            }
        });
        mEngine.getLevel(levelString);


    }

}