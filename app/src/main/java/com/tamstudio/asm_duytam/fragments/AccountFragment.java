package com.tamstudio.asm_duytam.fragments;

import static com.tamstudio.asm_duytam.services.UserService.ACTION_UPDATE_USER;
import static com.tamstudio.asm_duytam.utilities.MyStructure.KEY_RESULT;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_IS_LOGGED;
import static com.tamstudio.asm_duytam.utilities.MyStructure.SHARE_PREFERENCE_LOGIN_SOCIAL;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tamstudio.asm_duytam.R;
import com.tamstudio.asm_duytam.activities.LoginActivity;
import com.tamstudio.asm_duytam.dao.UserDAO;
import com.tamstudio.asm_duytam.model.User;
import com.tamstudio.asm_duytam.services.UserService;
import com.tamstudio.asm_duytam.utilities.MySharePreferences;
import com.tamstudio.asm_duytam.utilities.MyStructure;
import com.tamstudio.asm_duytam.utilities.Utilities;

import java.util.Objects;

public class AccountFragment extends Fragment {
    View mView;
    TextView tvEdit,tvLogout;
    Button btnUpdate;
    TextInputEditText edtName,edtEmail,edtPass,edtRePass;
    TextInputLayout inputRepass,inputEmail,inputPass;
    SwitchMaterial switchRole;
    User user;
    // nếu login bằng social thì ko hiện  form thay đổi mật khẩu
    boolean isLoginSocial;
    BroadcastReceiver userBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                if(intent.getAction().equals(ACTION_UPDATE_USER)){
                    boolean result = intent.getBooleanExtra(KEY_RESULT,false);
                    String mess;
                    if(result){
                        mess = MyStructure.TOAST_UPDATE_SUCCESS;
                        initDataUser();
                        setEnabledUI(false);
                        setVisibilityView(View.GONE);
                        tvEdit.setVisibility(View.VISIBLE);
                        inputEmail.setBackgroundResource(R.color.white);
                    }else{
                        mess = MyStructure.TOAST_UPDATE_NOT_SUCCESS;
                    }
                    Utilities.showToast(getContext(),mess,
                            result? R.drawable.ic_ok_toast:R.drawable.ic_delete_toast,
                            result? R.drawable.bg_ok_toast: R.drawable.bg_delete_toast);
                }
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account,container,false);
        initUI();
        initDataUser();
        events();
        return mView;
    }

    private void initUI() {
        tvEdit = mView.findViewById(R.id.tv_edit_account);
        tvLogout = mView.findViewById(R.id.tv_logout);
        btnUpdate = mView.findViewById(R.id.btn_update_account);
        edtName = mView.findViewById(R.id.edt_account_name);
        edtEmail = mView.findViewById(R.id.edt_account_email);
        edtPass = mView.findViewById(R.id.edt_account_pass);
        edtRePass = mView.findViewById(R.id.edt_account_pass_re);
        switchRole = mView.findViewById(R.id.switch_rule);
        inputRepass = mView.findViewById(R.id.ui_4);
        inputEmail = mView.findViewById(R.id.ui_1);
        inputPass = mView.findViewById(R.id.ui_3);
        setVisibilityView(View.GONE);
    }

    private void initDataUser() {
        String emailIdUser = MySharePreferences.getInstance(getContext()).getStringValue("email");
        user = UserDAO.getInstance(getContext()).getById(emailIdUser);
        if(user!=null){
            edtName.setText(user.getName());
            edtEmail.setText(user.getEmail());
            boolean isAdmin = user.isAdmin();
            switchRole.setChecked(isAdmin);
        }
    }

    private void events() {
        tvLogout.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn đăng xuất không ?")
                    .setPositiveButton("Đăng xuất", (dialogInterface, i) -> {
                        MySharePreferences.getInstance(getContext()).putBooleanValue(SHARE_PREFERENCE_IS_LOGGED,false);
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                        requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                    })
                    .setNegativeButton("Hủy",null)
                    .show();
        });
        tvEdit.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setMessage("Bạn Sẽ không Thể Thay Đổi Email")
                    .setPositiveButton("Tôi đã hiểu",null)
                    .show();
            setVisibilityView(View.VISIBLE);
            inputEmail.setBackgroundColor(Color.parseColor("#E8EAED"));
            edtPass.setText(null);
            edtRePass.setText(null);
            setEnabledUI(true);
            tvEdit.setVisibility(View.GONE);
        });
        btnUpdate.setOnClickListener(view -> {
            String newName = edtName.getText().toString();
            // login social ko cập nhật mk
            if(!isLoginSocial){
                String pass = edtPass.getText().toString();
                String rePass = edtRePass.getText().toString();
                if(!isValidation(newName,pass,rePass)){
                    return;
                }
                user.setPassword(pass);
            }

            boolean isAdmin = switchRole.isChecked();

            user.setName(newName);
            user.setAdmin(isAdmin);
            Intent intent = new Intent(ACTION_UPDATE_USER,null,requireContext(), UserService.class);
            intent.putExtra("user",user);
            requireActivity().startService(intent);

        });
    }
    public void setVisibilityView(int visibility){
        btnUpdate.setVisibility(visibility);
        if(isLoginSocial){
            inputPass.setVisibility(View.GONE);
            inputRepass.setVisibility(View.GONE);
        }else{
            inputPass.setVisibility(visibility);
            inputRepass.setVisibility(visibility);
        }

    }
    private boolean isValidation(String name,String newPass1,String newPass2) {
        if(name.trim().isEmpty()|| newPass1.trim().isEmpty()|| newPass2.trim().isEmpty()){
            Toast.makeText(getContext(),"Vui lòng điền đầy đủ thông tin",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(name.trim().length()<4|| newPass1.trim().length()<4){
            Toast.makeText(getContext(),"Tên và mật khẩu phải trên 4 kí tự",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!newPass1.equals(newPass2)){
            Toast.makeText(getContext(),"Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;
    }

    private void setEnabledUI(boolean enabled){
        edtName.setEnabled(enabled);
        edtRePass.setEnabled(enabled);
        edtPass.setEnabled(enabled);
        switchRole.setEnabled(enabled);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoginSocial = MySharePreferences.getInstance(requireContext()).getBooleanValue(SHARE_PREFERENCE_LOGIN_SOCIAL);
        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_USER);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(userBroadcast,intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(userBroadcast);
    }
}
