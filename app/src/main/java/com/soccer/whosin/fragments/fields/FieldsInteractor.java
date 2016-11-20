package com.soccer.whosin.fragments.fields;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.IFieldsInteractor;
import com.soccer.whosin.interfaces.IFieldsPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario on 19/11/16.
 **/

public class FieldsInteractor implements IFieldsInteractor {

    private IFieldsPresenter mPresenter;

    public FieldsInteractor(IFieldsPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getFields(String pFacebookId, String pGroupId) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.getFields(pGroupId).enqueue(new Callback<List<SoccerField>>() {
            @Override
            public void onResponse(Call<List<SoccerField>> call, Response<List<SoccerField>> response) {
                if (response.isSuccessful())
                    mPresenter.onGetFieldsSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onGetFieldsFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<SoccerField>> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onGetFieldsFailed(errorMessage);
            }
        });
    }

    @Override
    public void createField(String pFacebookId, SoccerField pSoccerField) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.createSoccerField(pSoccerField).enqueue(new Callback<SoccerField>() {
            @Override
            public void onResponse(Call<SoccerField> call, Response<SoccerField> response) {
                if (response.isSuccessful())
                    mPresenter.onFieldCreatedSuccessfully(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onFieldCreationFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SoccerField> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onFieldCreationFailed(errorMessage);
            }
        });
    }
}
