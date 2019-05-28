package com.topjohnwu.magisk.components;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.topjohnwu.core.tasks.MagiskInstaller;
import com.topjohnwu.core.utils.Utils;
import com.topjohnwu.magisk.R;
import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.io.SuFile;

import androidx.annotation.NonNull;

public class EnvFixDialog extends CustomAlertDialog {

    public EnvFixDialog(@NonNull Activity activity) {
        super(activity);
        setTitle(R.string.env_fix_title);
        setMessage(R.string.env_fix_msg);
        setCancelable(true);
        setPositiveButton(R.string.yes, (d, i) -> {
            ProgressDialog pd = ProgressDialog.show(activity,
                    activity.getString(R.string.setup_title),
                    activity.getString(R.string.setup_msg));
            new MagiskInstaller() {
                @Override
                protected boolean operations() {
                    installDir = new SuFile("/data/adb/magisk");
                    Shell.su("rm -rf /data/adb/magisk/*").exec();
                    return extractZip() && Shell.su("fix_env").exec().isSuccess();
                }

                @Override
                protected void onResult(boolean success) {
                    pd.dismiss();
                    Utils.toast(success ? R.string.setup_done : R.string.setup_fail, Toast.LENGTH_LONG);
                }
            }.exec();
        });
        setNegativeButton(R.string.no_thanks, null);
    }
}
