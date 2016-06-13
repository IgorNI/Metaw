package com.mbientlab.metawear.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.app.help.HelpOptionAdapter;

/**
 * Created by nilif on 2016/5/18.
 */
public class LoginFragment extends ModuleFragmentBase implements View.OnClickListener {
    private static final String TAG = "click";
    private Button LoginBtn;
    private Button RegisterBtn;
    private EditText et_phoneNum;
    private EditText et_password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        LoginBtn = (Button) v.findViewById(R.id.login_btn);
        RegisterBtn = (Button) v.findViewById(R.id.register_btn);
        et_phoneNum = (EditText) v.findViewById(R.id.input_phoneNum);
        et_password = (EditText) v.findViewById(R.id.input_password);
        LoginBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        return v;
    }

    @Override
    protected void boardReady() throws UnsupportedModuleException {
    }

    @Override
    protected void fillHelpOptionAdapter(HelpOptionAdapter adapter) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if (et_phoneNum.length() == 0 || et_password.length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(), "请输入电话或者密码", Toast.LENGTH_SHORT).show();
                }else {
                    // 与服务器端进行匹配，如果相同，则ok，
                    Intent intent = new Intent(getActivity(),MyInforActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.register_btn:
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
