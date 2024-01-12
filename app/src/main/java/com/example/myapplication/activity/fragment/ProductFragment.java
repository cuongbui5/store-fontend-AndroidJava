package com.example.myapplication.activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.AppActivity;
import com.example.myapplication.adapter.PageAdapter;
import com.example.myapplication.adapter.ProductAdapter;

import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiClientInstance;
import com.example.myapplication.listener.OnItemProductClickListener;
import com.example.myapplication.listener.OnPageClickListener;
import com.example.myapplication.model.Category;
import com.example.myapplication.model.Product;
import com.example.myapplication.model.SortObject;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.response.ProductResponse;
import com.example.myapplication.response.ResponseCategoryData;
import com.example.myapplication.response.ResponseProductData;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.Message;
import com.example.myapplication.util.SharedPreferencesManager;
import com.example.myapplication.util.UserDetail;
import com.example.myapplication.util.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment implements OnPageClickListener,FragmentScreen {


    private RecyclerView productContainer;
    private RecyclerView paginationRecyclerView;
    private TextView message;
    private ImageView btnLogout;
    private androidx.appcompat.widget.SearchView searchView;
    private Spinner spinnerCategory;
    private Spinner spinnerPriceSort;
    private ProgressBar progressBar;
    private String searchStr=null;
    private Long categoryId= 0L;
    private int pageCurrent=1;
    private int totalPage=0;
    private PageAdapter pageAdapter;
    private final OnItemProductClickListener onItemProductClickListener;

    private String sort="";
    private List<Product> products=new ArrayList<>();






    public ProductFragment(OnItemProductClickListener onItemProductClickListener) {
        this.onItemProductClickListener=onItemProductClickListener;
    }

    public void logout(){
        SharedPreferencesManager sharedPreferencesManager=new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.clearData();
        Intent intent=new Intent(getActivity(), MainActivity.class);
        requireActivity().startActivity(intent);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_product,container,false);
        initComponents(view);
        loadProduct();
        try {
            loadCategory();
        } catch (IOException e) {
            e.printStackTrace();

        }

        btnLogout.setOnClickListener(v->{
            showLogoutConfirmationDialog();
        });
        initSearchView();
        initSortSpinner();

        return view;
    }


    private void initSearchView(){
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()&& !query.equals(searchStr)){
                    pageCurrent=1;
                    searchStr=query;
                    Message.showToast(getContext(),"onCreateView search:Load product");
                    loadProduct();


                }else if (query.isEmpty() && searchStr != null) {
                    pageCurrent=1;
                    searchStr = null;
                    Message.showToast(getContext(),"onCreateView search:Load product");
                    loadProduct();
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals(searchStr)) {


                    if (!newText.isEmpty()) {
                        pageCurrent=1;
                        searchStr = newText;
                        loadProduct();
                    } else {
                        pageCurrent=1;
                        searchStr = null;
                        loadProduct();
                    }
                }

                return false;
            }
        });


    }

    private void initSortSpinner(){
        List<SortObject> sortObjects = new ArrayList<>();
        sortObjects.add(new SortObject("","","Default"));
        sortObjects.add(new SortObject("","price","↑̲ Price"));
        sortObjects.add(new SortObject("-","price","↓̲ Price"));
        ArrayAdapter<SortObject> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortObjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceSort.setAdapter(adapter);
        spinnerPriceSort.setSelection(0,false);
        spinnerPriceSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortObject sortObject= (SortObject) spinnerPriceSort.getSelectedItem();
                pageCurrent=1;
                if(Objects.equals(sortObject.getShow(), "default")){
                    sort="";
                }else {
                    sort=sortObject.getValue()+sortObject.getField();

                }
                loadProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Bạn có muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> logout())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }




    private void loadCategory() throws IOException {

        ApiClientInstance.getInstance(getContext()).getCategories().enqueue(new Callback<ResponseCategoryData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCategoryData> call, @NonNull Response<ResponseCategoryData> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body().getData();
                    Category category=new Category(0L,"All");
                    categories.add(0,category);
                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                    spinnerCategory.setSelection(0,false);
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
                            pageCurrent=1;
                            categoryId=selectedCategory.getId();
                            loadProduct();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {


                        }
                    });



                }
            }

            @Override
            public void onFailure(Call<ResponseCategoryData> call, Throwable t) {

            }
        });
    }


    private void showProduct(){
        if(products==null||products.isEmpty()){
            notFoundProduct();
        }else {
            productContainer.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            ProductAdapter productAdapter = new ProductAdapter(products,onItemProductClickListener);
            productContainer.setAdapter(productAdapter);
            showPage();
        }




    }

    private void showPage(){
        if(pageAdapter==null||pageAdapter.getTotalPage()!=totalPage){
            pageAdapter=new PageAdapter(totalPage,pageCurrent,this);
        }
        pageAdapter.setPageCurrent(pageCurrent);
        paginationRecyclerView.setVisibility(View.VISIBLE);
        paginationRecyclerView.setAdapter(pageAdapter);


    }

    private void notFoundProduct(){
        paginationRecyclerView.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);

    }




    public void loadProduct(){
        progressBar.setVisibility(View.VISIBLE);
        productContainer.setVisibility(View.GONE);
        ApiClient apiClient=ApiClientInstance.getInstance(getContext());
        Call<ResponseProductData> call;
        if(searchStr==null&&categoryId==0L){
            if(!Objects.equals(sort, "")){
                call=apiClient.getProductsBySort(pageCurrent, Constant.sizeDefault,sort);
            }else {
                call=apiClient.getProducts(pageCurrent, Constant.sizeDefault);
            }

        }else if (categoryId!=0&&searchStr==null){
            if(!Objects.equals(sort, "")){
                call=apiClient.getProductsBySortAndCategory(pageCurrent, Constant.sizeDefault,sort,categoryId);
            }else {
                call=apiClient.getProductByCategory(pageCurrent, Constant.sizeDefault,categoryId);
            }

        } else if (categoryId == 0) {
            if(!Objects.equals(sort, "")){
                call=apiClient.getProductsBySortAndName(pageCurrent, Constant.sizeDefault,sort,searchStr);
            }else {
                call=apiClient.getProductByName(pageCurrent, Constant.sizeDefault,searchStr);
            }

        }else {
            if(!Objects.equals(sort, "")){
                call=apiClient.getProductsBySortAndNameAndCategory(pageCurrent, Constant.sizeDefault,sort,searchStr,categoryId);
            }else {
                call=apiClient.getProductByCategoryAnName(pageCurrent, Constant.sizeDefault,categoryId,searchStr);
            }

        }

        call.enqueue(new Callback<ResponseProductData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseProductData> call, @NonNull Response<ResponseProductData> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    ResponseProductData res=response.body();
                    if(res!=null){
                        ProductResponse data=res.getData();
                        products=data.getProducts();
                        totalPage=data.getTotalPage();
                        pageCurrent=data.getPageIndex();
                    }
                    showProduct();




                }else {
                    Utils.handlerErrorResponse(response,getContext());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseProductData> call, @NonNull Throwable t) {
                Log.e("Product Fragment", Objects.requireNonNull(t.getMessage()));

            }

        });



    }


    @Override
    public void onPageClick(int page) {
        if(pageCurrent==page){
            return;
        }
        pageCurrent=page;
        loadProduct();
    }




    @Override
    public void initComponents(View view) {
        productContainer = view.findViewById(R.id.product_container);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        productContainer.setLayoutManager(gridLayoutManager);
        paginationRecyclerView = view.findViewById(R.id.paginationRecyclerView);
        LinearLayoutManager paginationLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        paginationRecyclerView.setLayoutManager(paginationLayoutManager);
        TextView username = view.findViewById(R.id.txtUsernameProduct);
        btnLogout = view.findViewById(R.id.btnLogout);
        searchView = view.findViewById(R.id.searchView);
        message = view.findViewById(R.id.txtMessageLoadProduct);
        spinnerCategory = view.findViewById(R.id.spinner);
        progressBar = view.findViewById(R.id.progressBar);
        spinnerPriceSort=view.findViewById(R.id.spinnerPrice);
        username.setText(UserDetail.username);
       

    }
}
