package com.example.user.comprarcafe.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.comprarcafe.Fragments.Fragment_cafe_pasilla;
import com.example.user.comprarcafe.Fragments.Fragment_cafe_seco;
import com.example.user.comprarcafe.Fragments.Fragment_cafe_verde;

public class Adapter_view_pager extends FragmentPagerAdapter {

    //Número de Pestañas
    int numeroDePestañas;

    public Adapter_view_pager(FragmentManager fm, int numeroDePestañas) {
        super(fm);
        this.numeroDePestañas = numeroDePestañas;
    }

    //Me retorna el fragmento que necesito de acuerdo al item de pestaña que se escoja
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new Fragment_cafe_seco();
            case 1:
                return new Fragment_cafe_verde();
            case 2:
                return new Fragment_cafe_pasilla();
            default:
                return null;
        }
    }

    @Override
    //El dato de las pestañas se recibe en la actividad principal
    public int getCount() {
        return numeroDePestañas;
    }

    //Me retorna el título de cada una de las pestañas
    @Override
    public CharSequence getPageTitle(int position){
        switch (position) {

            case 0:
                return "Café Seco";
            case 1:
                return "Café Verde";
            case 2:
                return "Café Pasilla";
            default:
                return null;
        }
    }
}