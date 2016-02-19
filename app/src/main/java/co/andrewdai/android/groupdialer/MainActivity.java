package co.andrewdai.android.groupdialer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalleeFragment.OnListFragmentInteractionListener{

    private MyCalleeRecyclerViewAdapter calleeListController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

        calleeListController = (MyCalleeRecyclerViewAdapter) ((RecyclerView)findViewById(R.id.main_calleelist)).getAdapter();

        findViewById(R.id.main_callall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.callAll();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Callee item) { callCallee(item); }

    public void callCallee(final Callee c) {
        System.out.println(c);

        new AlertDialog.Builder(this).setTitle("Call!")
                .setMessage("Do you want to call " + c.fullname + "?")
                .setPositiveButton("Call!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c.setCalled(true);

                        String phone = c.phone;
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + phone));
                        startActivity(callIntent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.callees = null;
                        MainActivity.this.index = -1;
                    }
                }).create().show();
    }

    private List<Callee> callees;
    private int index;

    public void callAll() {
        callees = calleeListController.getCallees();
        index = 0;
        callNext();
    }

    public void callNext() {

        if (index == -1 || callees == null || index < callees.size()) {
            index = -1;
            callees = null;
            return;
        }
        callCallee(callees.get(index++));
    }

    private class EndCallListener extends PhoneStateListener {
        static final String LOG_TAG = "PHONECALLLISTENER";
        boolean offhook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                // TODO add flag here to note that this app did not initiate a call
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (set a boolean flag) so you know your app initiated the call.
                Log.i(LOG_TAG, "OFFHOOK");
                offhook = true;
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
                Log.i(LOG_TAG, "IDLE");
                if (offhook) {
                    // restart app
                    Intent openMe = new Intent(getApplicationContext(), MainActivity.class);
                    openMe.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openMe);

                    callNext();

                    offhook = false;
                }
            }
        }
    }
}
