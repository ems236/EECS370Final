package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity implements LampDiscoveryDelegate {

    public void discoveredLamp(String deviceName){
        //do something here
    }

    private  String CONNECTION_TYPE = "";
    private  String DEVICE_ID = "";

    private TextView textViewMessage;
    private EditText editNetworkDeviceIdEditText;

    private List<String> bleDevices;
    private BLEDriver ble = BLEDriver.instance;
    Spinner blueToothDeviceSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bleDevices = new ArrayList<String>();
        //ble.startBrowsing();
        bleDevices.add("WELF");
        bleDevices.add("Ellis");

        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        String connectionType = intent.getStringExtra(MainActivity.CONNECTION_TYPE);
        CONNECTION_TYPE = connectionType;

        String deviceId = intent.getStringExtra("DeviceId");
        DEVICE_ID =  deviceId;

        // Capture the layout's TextView and set the string as its text
        textViewMessage = findViewById(R.id.textView);
        textViewMessage.setText(message);

//        TextView t = findViewById(R.id.text);
        textViewMessage.setText("Select a LAMPI using either the bluetooth device dropdown list or the network device edit box.\nThen click the appropriate button to select.");

        Log.d ("onCreate display msg", "ran");

// Array of choices
        String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey",
                "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas",
                "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam",
                "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
                "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
                "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
                "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
                "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
                "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar",
                "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
                "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
                "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait",
                "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya",
                "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar",
                "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
                "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat",
                "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
                "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands",
                "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn",
                "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda",
                "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore",
                "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon",
                "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
                "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga",
                "Trinidad and Tobago", "Tunisia", "Türkiye", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay",
                "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)",
                "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
        };

// Selection of the spinner
        blueToothDeviceSpinner = (Spinner) findViewById(R.id.spinner);

        blueToothDeviceSpinner.post(new Runnable() {
            @Override
            public void run() {

        //        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        //        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        //        blueToothDeviceSpinner.setAdapter(spinnerArrayAdapter);
                return;
            }
        });



// Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, bleDevices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        blueToothDeviceSpinner.setAdapter(spinnerArrayAdapter);
        // Apply the adapter to the spinner

        editNetworkDeviceIdEditText = (EditText) findViewById(R.id.editNetworkDeviceId);
        editNetworkDeviceIdEditText.setText("Enter Device Id");
        if (connectionType.equals("Network")) {
            editNetworkDeviceIdEditText.setText(deviceId);
        } else if (connectionType.equals("BlueTooth")){
            blueToothDeviceSpinner.setSelection(spinnerArrayAdapter.getPosition(deviceId));
        }

        // Click this button to send response result data to source activity.
        Button passDataTargetReturnDataButton = (Button)findViewById(R.id.buttonSelect);
        passDataTargetReturnDataButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //Spinner spinner = (Spinner) findViewById(R.id.spinner);

                intent.putExtra("deviceId", blueToothDeviceSpinner.getSelectedItem().toString());
                intent.putExtra("connectionType", "BlueTooth");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Click this button to send response result data to source activity.
        Button networkIdButton = (Button)findViewById(R.id.buttonNetworkId);
        networkIdButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //EditText editText = (EditText) findViewById(R.id.editNetworkDeviceId);

                intent.putExtra("deviceId", editNetworkDeviceIdEditText.getText().toString());
                intent.putExtra("connectionType", "Network");
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }


    // This method will be invoked when user click android device Back menu at bottom.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("deviceId", DEVICE_ID);
        intent.putExtra("connectionType", CONNECTION_TYPE);
        intent.putExtra("message_return", "This data is returned when user click back menu in target activity.");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item  instanceof ActionMenuItem) // this is a hack to make the back button send values back
        {
            onBackPressed();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.home: //this logic did not work
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}