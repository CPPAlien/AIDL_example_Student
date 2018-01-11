package me.pengtao.studenta;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.pengtao.rule.ITeacher;
import me.pengtao.rule.StudentInfo;

public class MainActivity extends AppCompatActivity {
    private ITeacher mTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent().setComponent(new ComponentName("me.pengtao.teacher",
                "me.pengtao.teacher.TeacherService"));
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            mTeacher = ITeacher.Stub.asInterface(service);

            StudentInfo myInfo = new StudentInfo()
                    .setPackageId(BuildConfig.APPLICATION_ID)
                    .setAppName(getString(R.string.app_name))
                    .setSex("M");
            try {
                mTeacher.registerId(BuildConfig.APPLICATION_ID);
                mTeacher.registerInfo(myInfo);
                ((TextView) findViewById(R.id.alias)).setText(getString(R.string.alias, mTeacher
                        .getAlias()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mTeacher = null;
        }
    };
}
