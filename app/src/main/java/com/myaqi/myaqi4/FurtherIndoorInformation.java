package com.myaqi.myaqi4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FurtherIndoorInformation extends AppCompatActivity {
    TextView mainDesc, indoorDesc1, indoorDesc2, indoorDesc3, indoorDesc4, indoorDesc5, indoorDesc6;
    Button btnAsthma, btnCOPD, btnAllergies, btnBronchitis;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_further_indoor_information);

        FloatingActionButton backButton = findViewById(R.id.backToIndoor);
        mainDesc = findViewById(R.id.titleDescription);
        indoorDesc1 = findViewById(R.id.indoorDesc1);
        indoorDesc2 = findViewById(R.id.indoorDesc2);
        indoorDesc3 = findViewById(R.id.indoorDesc3);
        indoorDesc4 = findViewById(R.id.indoorDesc4);
        indoorDesc5 = findViewById(R.id.indoorDesc5);
        indoorDesc6 = findViewById(R.id.subDescription);
        btnAsthma = findViewById(R.id.btnAsthma);
        btnCOPD = findViewById(R.id.btnCOPD);
        btnAllergies = findViewById(R.id.btnAllergies);
        btnBronchitis = findViewById(R.id.btnBronchitis);
        String desc = getDesc();
        //String desc = "UNHEALTHY FOR SENSITIVE GROUPS";
        mainDesc.setText(desc);
        mainDesc.setGravity(Gravity.CENTER);



        if (desc.equals("GOOD")) {
            indoorDesc1.setText("- Encourage students to engage in regular indoor activities without restrictions.");
            indoorDesc2.setText("- Ensure proper ventilation in classrooms and common areas to maintain good indoor air quality.");
            indoorDesc3.setText("- Educate students about the importance of fresh air and its impact on overall well-being.");
            indoorDesc4.setText("- Promote healthy habits such as regular exercise, balanced nutrition, and staying hydrated to support overall health.");
            indoorDesc5.setText("");
        } else if (desc.equals("MODERATE")) {
            indoorDesc1.setText("- Consider adjusting or rescheduling strenuous physical activities for students indoors.");
            indoorDesc2.setText("- Monitor the Air Quality Index (AQI) regularly and modify activities accordingly.");
            indoorDesc3.setText("- Teach students about the potential effects of moderate indoor air quality on their health.");
            indoorDesc4.setText("- Encourage students to take breaks and practice deep breathing exercises to enhance indoor air circulation.");
            indoorDesc5.setText("");
        } else if (desc.equals("UNHEALTHY FOR SENSITIVE GROUPS")) {
            indoorDesc1.setText("- Modify or reschedule strenuous activities for all students indoors.");
            indoorDesc2.setText("- Raise awareness about the impact of poor indoor air quality on overall health and well-being.");
            indoorDesc3.setText("- Encourage students to report any symptoms, such as coughing or irritation, to their teachers or school staff.");
            indoorDesc4.setText("- Collaborate with healthcare professionals to provide general education on maintaining a healthy indoor environment.");
            indoorDesc5.setText("");
        } else if (desc.equals("UNHEALTHY")) {
            indoorDesc1.setText("- Advise students to minimize physical exertion and avoid strenuous activities indoors.");
            indoorDesc2.setText("- Educate students about the potential health risks associated with poor indoor air quality.");
            indoorDesc3.setText("- Encourage students to stay hydrated and take breaks in well-ventilated areas.");
            indoorDesc4.setText("- Collaborate with healthcare professionals to provide guidance and resources on maintaining respiratory health.");
            indoorDesc5.setText("");
        } else if (desc.equals("VERY UNHEALTHY")) {
            indoorDesc1.setText("- Inform students about the severe health risks associated with very unhealthy indoor air quality.");
            indoorDesc2.setText("- Encourage students to minimize exposure to the indoor environment and seek fresh air when possible.");
            indoorDesc3.setText("- Raise awareness about the importance of respiratory protection, such as wearing masks if necessary.");
            indoorDesc4.setText("- Collaborate with healthcare professionals to provide comprehensive guidance and support for students during this critical period.");
            indoorDesc5.setText("");
        } else if (desc.equals("HAZARDOUS")) {
            indoorDesc1.setText("- Inform students about the hazardous nature of the indoor air quality and the immediate risks it poses to health.");
            indoorDesc2.setText("- Initiate emergency measures, such as evacuating the premises or seeking alternative locations with better air quality.");
            indoorDesc3.setText("- Encourage students to seek immediate medical attention if they experience any symptoms related to poor air quality.");
            indoorDesc4.setText("- Collaborate with healthcare professionals to provide necessary medical interventions and support for students affected by the hazardous indoor air quality situation.");
            indoorDesc5.setText("");
        }



        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        int hourlyAvg = sp.getInt("hourlyAvg", 0);
        int curr = sp.getInt("curr", 0);
        int dailyAvg = sp.getInt("dailyAvg", 0);
        String str = "";
        if (dailyAvg > hourlyAvg + 5 && hourlyAvg > curr + 5) {
            str = "decreasing";
        }
        else if (curr > hourlyAvg + 5 && hourlyAvg > dailyAvg + 5) {
            str = "increasing";
        }
        else if (Math.abs(curr-hourlyAvg) < 8 && Math.abs(hourlyAvg-dailyAvg) < 8){
            str = "roughly the same";
        }
        else {
            str = "fluctuating";
        }


        if (curr <= 50) {
            mainDesc.setBackgroundResource(R.color.aqi_good);
        } else if (curr <= 100) {
            mainDesc.setBackgroundResource(R.color.aqi_moderate);
        } else if (curr <= 150) {
            mainDesc.setBackgroundResource(R.color.aqi_unhealthy_for_sensitive_groups);
        } else if (curr <= 200) {
            mainDesc.setBackgroundResource(R.color.aqi_unhealthy);
        } else if (curr <= 300) {
            mainDesc.setBackgroundResource(R.color.aqi_very_unhealthy);
        } else {
            mainDesc.setBackgroundResource(R.color.aqi_hazardous);
        }

        indoorDesc6.setText("Based on current trends, the AQI is " + str + " over time ");

        btnAsthma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.equals("GOOD")) {
                    indoorDesc1.setText("- Asthmatic students can participate in regular indoor activities without significant restrictions.");
                    indoorDesc2.setText("- Ensure proper ventilation in classrooms and common areas to maintain good indoor air quality.");
                    indoorDesc3.setText("- Educate students and staff about asthma triggers, early warning signs, and the importance of proper inhaler usage.");
                    indoorDesc4.setText("- Encourage asthmatic students to carry their rescue inhalers at all times and use them as prescribed.");
                    indoorDesc5.setText("- Promote a healthy lifestyle with regular exercise, proper nutrition, and adequate hydration to support respiratory health.");
                } else if (desc.equals("MODERATE")) {
                    indoorDesc1.setText("- Consider modifying or rescheduling strenuous physical activities for asthmatic students indoors.");
                    indoorDesc2.setText("- Monitor the AQI regularly and adjust activities accordingly.");
                    indoorDesc3.setText("- Encourage asthmatic students to practice controlled breathing techniques to manage symptoms during physical exertion.");
                    indoorDesc4.setText("- Ensure that all staff members are trained in recognizing and responding to asthma symptoms and emergencies.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to provide asthma education sessions for students, staff, and parents.");
                } else if (desc.equals("UNHEALTHY FOR SENSITIVE GROUPS")) {
                    indoorDesc1.setText("- Modify or reschedule strenuous activities for asthmatic students indoors.");
                    indoorDesc2.setText("- Ensure that asthmatic students carry their rescue inhalers and have access to them at all times.");
                    indoorDesc3.setText("- If students are experiencing coughing or throat irritation, advise families to contact a medical professional.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to develop individualized asthma action plans for each asthmatic student.");
                } else if (desc.equals("UNHEALTHY")) {
                    indoorDesc1.setText("- Students should avoid strenuous activities or any physical exertion indoors.");
                    indoorDesc2.setText("- Asthmatic students' rescue inhalers are now necessary, ensure that they are carrying them.");
                    indoorDesc3.setText("- Exposure over extended periods of time is approximately equivalent to smoking one cigarette.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised for students to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to conduct regular asthma check-ups and provide additional support as needed.");
                } else if (desc.equals("VERY UNHEALTHY")) {
                    indoorDesc1.setText("- Exposure over extended periods of time is approximately equivalent to smoking nine cigarettes.");
                    indoorDesc2.setText("- Installing air purification devices is recommended.");
                    indoorDesc3.setText("- It is critical for students to carry a rescue inhaler.");
                    indoorDesc4.setText("- Have school nursing staff monitor asthmatic students.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to develop individualized asthma action plans for students with severe asthma and provide additional support during this critical period.");
                } else if (desc.equals("HAZARDOUS")) {
                    indoorDesc1.setText("- Exposure over extended periods of time is approximately equivalent to smoking twenty-four cigarettes.");
                    indoorDesc2.setText("- Installing air purification devices is critical.");
                    indoorDesc3.setText("- It is critical for students to carry a rescue inhaler.");
                    indoorDesc4.setText("- Students must seek medical attention if experiencing any symptoms, even mild.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to provide comprehensive asthma management strategies for students and support their specific medical needs.");
                }
            }
        });

        btnCOPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.equals("GOOD")) {
                    indoorDesc1.setText("- Students with COPD can participate in regular indoor activities without significant restrictions.");
                    indoorDesc2.setText("- Ensure proper ventilation in classrooms and common areas to maintain good indoor air quality.");
                    indoorDesc3.setText("- Educate students and staff about COPD triggers, early warning signs, and the importance of medication adherence.");
                    indoorDesc4.setText("- Encourage students with COPD to carry their prescribed medications at all times and use them as directed.");
                    indoorDesc5.setText("- Promote a healthy lifestyle with regular exercise, proper nutrition, and adequate hydration to support respiratory health.");
                } else if (desc.equals("MODERATE")) {
                    indoorDesc1.setText("- Consider modifying or rescheduling strenuous physical activities for students with COPD indoors.");
                    indoorDesc2.setText("- Monitor the AQI regularly and adjust activities accordingly.");
                    indoorDesc3.setText("- Encourage controlled breathing techniques to manage symptoms during physical exertion.");
                    indoorDesc4.setText("- Ensure that all staff members are trained in recognizing and responding to COPD symptoms and emergencies.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to provide COPD education sessions for students, staff, and parents.");
                } else if (desc.equals("UNHEALTHY FOR SENSITIVE GROUPS")) {
                    indoorDesc1.setText("- Modify or reschedule strenuous activities for students with COPD indoors.");
                    indoorDesc2.setText("- Ensure that students with COPD carry their prescribed medications and have access to them at all times.");
                    indoorDesc3.setText("- If students are experiencing increased breathlessness or coughing, advise families to contact a medical professional.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to develop individualized COPD management plans for each student.");
                } else if (desc.equals("UNHEALTHY")) {
                    indoorDesc1.setText("- Students with COPD should avoid strenuous activities or any physical exertion indoors.");
                    indoorDesc2.setText("- Ensure that students with COPD have their prescribed medications readily available.");
                    indoorDesc3.setText("- Exposure over extended periods of time is approximately equivalent to smoking one cigarette.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised for students to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to conduct regular check-ups and provide additional support for students with COPD.");
                } else if (desc.equals("VERY UNHEALTHY")) {
                    indoorDesc1.setText("- Exposure over extended periods of time is approximately equivalent to smoking nine cigarettes.");
                    indoorDesc2.setText("- Installing air purification devices is recommended.");
                    indoorDesc3.setText("- It is critical for students with COPD to carry their prescribed medications at all times.");
                    indoorDesc4.setText("- Have school nursing staff monitor students with COPD.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to develop individualized COPD management plans for students with severe COPD and provide additional support during this critical period.");
                } else if (desc.equals("HAZARDOUS")) {
                    indoorDesc1.setText("- Exposure over extended periods of time is approximately equivalent to smoking twenty-four cigarettes.");
                    indoorDesc2.setText("- Installing air purification devices is critical.");
                    indoorDesc3.setText("- It is critical for students with COPD to carry their prescribed medications at all times.");
                    indoorDesc4.setText("- Students must seek immediate medical attention if experiencing any symptoms, even mild.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to provide intensive monitoring and support for students with COPD and ensure emergency response protocols are in place.");
                }
            }
        });

//add cleaning vacumming to each
        btnAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.equals("GOOD")) {
                    indoorDesc1.setText("- Students with allergies can participate in regular indoor activities without significant restrictions.");
                    indoorDesc2.setText("- Ensure proper ventilation in classrooms and common areas to maintain good indoor air quality.");
                    indoorDesc3.setText("- Regularly clean and vacuum classrooms to minimize allergens like dust mites, pet dander, and pollen.");
                    indoorDesc4.setText("- Encourage students to practice good hygiene, including frequent handwashing and avoiding touching their face.");
                    indoorDesc5.setText("- Collaborate with parents and healthcare professionals to develop individualized allergy management plans for each student.");
                } else if (desc.equals("MODERATE")) {
                    indoorDesc1.setText("- Consider modifying or rescheduling outdoor activities when allergen levels are high.");
                    indoorDesc2.setText("- Monitor the AQI and pollen count regularly and adjust activities accordingly.");
                    indoorDesc3.setText("- Provide a designated area for students with allergies to take breaks and minimize exposure to allergens.");
                    indoorDesc4.setText("- Encourage students to use nasal sprays or antihistamines as prescribed by their healthcare provider.");
                    indoorDesc5.setText("- Collaborate with parents and healthcare professionals to identify specific allergens and develop strategies to minimize exposure.");
                } else if (desc.equals("UNHEALTHY FOR SENSITIVE GROUPS")) {
                    indoorDesc1.setText("- Modify or reschedule outdoor activities when allergen levels are high.");
                    indoorDesc2.setText("- Encourage students with allergies to carry their prescribed medications and have access to them at all times.");
                    indoorDesc3.setText("- If students are experiencing severe allergy symptoms, advise families to contact a medical professional.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to develop individualized allergy management plans for each student and provide additional support during high-allergen periods.");
                } else if (desc.equals("UNHEALTHY")) {
                    indoorDesc1.setText("- Students with allergies should minimize outdoor activities and stay indoors as much as possible.");
                    indoorDesc2.setText("- Ensure that students with allergies have their prescribed medications readily available.");
                    indoorDesc3.setText("- Exposure over extended periods of time may worsen allergy symptoms and respiratory distress.");
                    indoorDesc4.setText("- If outdoor air quality is better, it is advised for students to remain outdoors or open the windows.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to conduct regular check-ups and provide additional support for students with severe allergies.");
                } else if (desc.equals("VERY UNHEALTHY")) {
                    indoorDesc1.setText("- Minimize outdoor activities and keep students with allergies indoors, especially during peak allergen periods.");
                    indoorDesc2.setText("- Installing air purifiers and using high-efficiency filters can help reduce indoor allergens.");
                    indoorDesc3.setText("- Encourage students to wear masks outdoors to minimize exposure to allergens.");
                    indoorDesc4.setText("- Collaborate with parents and healthcare professionals to monitor allergy symptoms and adjust medication management accordingly.");
                    indoorDesc5.setText("- Provide a clean and allergen-free environment by regularly cleaning and maintaining HVAC systems and ensuring proper ventilation.");
                } else if (desc.equals("HAZARDOUS")) {
                    indoorDesc1.setText("- Keep students with allergies indoors and avoid outdoor activities completely during hazardous conditions.");
                    indoorDesc2.setText("- Use air purifiers with HEPA filters in classrooms to remove airborne allergens.");
                    indoorDesc3.setText("- Advise students to wear masks both indoors and outdoors to minimize allergen exposure.");
                    indoorDesc4.setText("- Collaborate with parents and healthcare professionals to develop emergency plans for severe allergy episodes.");
                    indoorDesc5.setText("- Consider implementing additional measures such as temporary closure or relocation of classrooms to minimize allergen exposure.");
                }
            }
        });

        btnBronchitis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desc.equals("GOOD")) {
                    indoorDesc1.setText("- Students with bronchitis can participate in regular indoor activities without significant restrictions.");
                    indoorDesc2.setText("- Ensure proper ventilation in classrooms and common areas to maintain good indoor air quality.");
                    indoorDesc3.setText("- Encourage students to practice good hygiene, including frequent handwashing and covering their mouth and nose when coughing or sneezing.");
                    indoorDesc4.setText("- Promote a clean and dust-free environment by regularly cleaning and vacuuming classrooms.");
                    indoorDesc5.setText("- Collaborate with parents and healthcare professionals to monitor and manage bronchitis symptoms in students.");
                } else if (desc.equals("MODERATE")) {
                    indoorDesc1.setText("- Consider modifying or rescheduling outdoor activities when air quality is moderate or worsens.");
                    indoorDesc2.setText("- Encourage students with bronchitis to carry their prescribed medications and have access to them at all times.");
                    indoorDesc3.setText("- Monitor air quality and limit exposure to outdoor pollutants by keeping windows closed and using air purifiers.");
                    indoorDesc4.setText("- Provide a designated area for students with bronchitis to take breaks and rest if needed.");
                    indoorDesc5.setText("- Collaborate with parents and healthcare professionals to develop individualized bronchitis management plans for each student.");
                } else if (desc.equals("UNHEALTHY FOR SENSITIVE GROUPS")) {
                    indoorDesc1.setText("- Modify or reschedule outdoor activities when air quality is unhealthy for sensitive groups.");
                    indoorDesc2.setText("- Ensure students with bronchitis have their prescribed medications readily available and use them as needed.");
                    indoorDesc3.setText("- If students experience increased coughing or breathing difficulties, advise families to seek medical attention.");
                    indoorDesc4.setText("- Monitor air quality and implement additional measures to improve indoor air, such as using air purifiers or keeping windows closed.");
                    indoorDesc5.setText("- Collaborate with healthcare professionals to regularly assess and manage bronchitis symptoms in students.");
                } else if (desc.equals("UNHEALTHY")) {
                    indoorDesc1.setText("- Students with bronchitis should minimize outdoor activities and stay indoors as much as possible.");
                    indoorDesc2.setText("- Ensure students have access to their prescribed medications and encourage proper usage.");
                    indoorDesc3.setText("- Monitor air quality and implement measures to improve indoor air, such as using air purifiers or keeping windows closed.");
                    indoorDesc4.setText("- Encourage students to cover their mouth and nose with a mask or scarf when exposed to poor air quality.");
                    indoorDesc5.setText("- Collaborate with parents, healthcare professionals, and school authorities to provide necessary support and accommodations for students with bronchitis.");
                } else if (desc.equals("VERY UNHEALTHY")) {
                    indoorDesc1.setText("- Keep students with bronchitis indoors and avoid outdoor activities completely during very unhealthy conditions.");
                    indoorDesc2.setText("- Use air purifiers with HEPA filters in classrooms to remove airborne pollutants.");
                    indoorDesc3.setText("- Encourage students to wear masks both indoors and outdoors to minimize exposure to pollutants.");
                    indoorDesc4.setText("- Collaborate with healthcare professionals to regularly assess bronchitis symptoms and adjust medication management as needed.");
                    indoorDesc5.setText("- Provide a clean and allergen-free environment by regularly cleaning and maintaining HVAC systems and ensuring proper ventilation.");
                } else if (desc.equals("HAZARDOUS")) {
                    indoorDesc1.setText("- Keep students with bronchitis indoors and avoid outdoor activities completely during hazardous conditions.");
                    indoorDesc2.setText("- Use air purifiers with HEPA filters and maintain proper ventilation to reduce indoor pollutants.");
                    indoorDesc3.setText("- Advise students to wear masks both indoors and outdoors to minimize exposure to hazardous pollutants.");
                    indoorDesc4.setText("- Collaborate with healthcare professionals to closely monitor bronchitis symptoms and adjust treatment plans accordingly.");
                    indoorDesc5.setText("- Consider temporary closure or relocation of classrooms if outdoor pollution levels are exceptionally high.");
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }


    public String getDesc() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String desc = sp.getString("desc", "GOOD");
        return desc;
    }


}