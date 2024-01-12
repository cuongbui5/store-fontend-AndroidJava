package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.listener.AddressItemClickListener;
import com.example.myapplication.model.AddressDto;

import java.util.List;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<AddressDto> addressDtos;
    private AddressItemClickListener addressItemClickListener;

    public AddressAdapter(List<AddressDto> addressDtos, AddressItemClickListener addressItemClickListener) {
        this.addressDtos = addressDtos;
        this.addressItemClickListener=addressItemClickListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressDto addressDto=addressDtos.get(position);
        Log.d("holder","City: "+addressDto.getCity().toString()+" "+"Number: "+addressDto.getApartmentNumber().toString()+" "+"Street: "+addressDto.getStreet().toString()+" default:"+addressDto.isCurrent());
        holder.city.setText("City: "+addressDto.getCity().toString());
        holder.number.setText("Number: "+addressDto.getApartmentNumber().toString());
        holder.street.setText("Street: "+addressDto.getStreet().toString());
        holder.btnSetDefault.setOnClickListener(v -> addressItemClickListener.setDefault(addressDto.getId()));
        holder.btnDelete.setOnClickListener(v->addressItemClickListener.deleteAddress(addressDto.getId()));
        holder.btnUpdate.setOnClickListener(v->addressItemClickListener.updateAddress(addressDto));
        if(addressDto.isCurrent()){
            holder.isDefault.setVisibility(View.VISIBLE);
            holder.btnSetDefault.setVisibility(View.GONE);
        }else {
            holder.isDefault.setVisibility(View.GONE);
            holder.btnSetDefault.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        if(addressDtos==null){
            return 0;
        }
        return addressDtos.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView number,street,city,isDefault;
        Button btnUpdate,btnDelete,btnSetDefault;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.textApartmentNumber);
            street=itemView.findViewById(R.id.textStreet);
            city=itemView.findViewById(R.id.textCity);
            isDefault=itemView.findViewById(R.id.textIsDefault);
            btnDelete=itemView.findViewById(R.id.btnDeleteAddress);
            btnUpdate=itemView.findViewById(R.id.btnUpdateAddress);
            btnSetDefault=itemView.findViewById(R.id.btnSetDefault);
        }
    }
}
