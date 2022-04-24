package com.example.myapplication.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.UserListAdapter;
import com.example.myapplication.models.UserModel;

import java.util.ArrayList;

public class Users extends Fragment {

    ArrayList<UserModel> arrayList = new ArrayList<>();




    public Users() {
    }

    public static Users newInstance() {
        Users fragment = new Users();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = FragmentUsersBinding.inflate(getLayoutInflater());
//        userViewModel = new UserViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.fragment_users,container,false);
        buildList();
        RecyclerView recyclerView = view.findViewById(R.id.users_RV);
        recyclerView.setAdapter(new UserListAdapter(arrayList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    private void buildList(){
        String address = "mipmap-xxhdpi/img.png";
        arrayList.add(new UserModel("Eljoo" , "Yasta ana b7bk yasta",address));
        arrayList.add(new UserModel("Samy" , "El3nkboot el nono",address));
        arrayList.add(new UserModel("Kimo" , "Yalla Valo",address));
        arrayList.add(new UserModel("Omar" , "Yalla Generalz",address));
        arrayList.add(new UserModel("Nofal" , "Ana mosh 3arefny ana toht mny",address));
        arrayList.add(new UserModel("Eljoo" , "Yasta ana b7bk yasta",address));
        arrayList.add(new UserModel("Samy" , "El3nkboot el nono",address));
        arrayList.add(new UserModel("Kimo" , "Yalla Valo",address));
        arrayList.add(new UserModel("Omar" , "Yalla Generalz",address));
        arrayList.add(new UserModel("Nofal" , "Ana mosh 3arefny ana toht mny",address));


    }
}