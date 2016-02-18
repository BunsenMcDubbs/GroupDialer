package co.andrewdai.android.groupdialer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements CalleeFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onListFragmentInteraction(Callee item) {
        System.out.println(item);

        item.setCalled(true);

        String phone = item.phone;
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone));
        startActivity(callIntent);
    }

    private class EndCallListener extends PhoneStateListener {
        static final String LOG_TAG = "PHONECALLLISTENER";
        boolean offhook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                Log.i(LOG_TAG, "OFFHOOK");
                offhook = true;
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
                Log.i(LOG_TAG, "IDLE");
                if (offhook) {
                    // restart app
                    Intent openMe = new Intent(getApplicationContext(), MainActivity.class);
                    openMe.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //experiment with the flags
                    startActivity(openMe);
                    offhook = false;
                }
            }
        }
    }
}
