/**
 * Created by nilif on 2016/5/3.
 */
package com.mbientlab.metawear.app;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.MetaWearBoard.DeviceInformation;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.Switch;
/*
* 显示主页
*
* */
public class HomeFragment extends ModuleFragmentBase {
    private Led ledModule;

    public static class MetaBootWarningFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_warning)
                    .setPositiveButton(R.string.label_ok, null)
                    .setCancelable(false)
                    .setMessage(R.string.message_metaboot)
                    .create();
        }
    }

    public HomeFragment() {
        super(R.string.navigation_fragment_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }



    @Override
    protected void boardReady() throws UnsupportedModuleException {
        /*setupFragment(getView());*/
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {

    }

    }

