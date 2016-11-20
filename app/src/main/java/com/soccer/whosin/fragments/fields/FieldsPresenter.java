package com.soccer.whosin.fragments.fields;

import com.soccer.whosin.interfaces.IFieldsPresenter;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.BusProvider;

import java.util.List;

/**
 * Created by Mario on 19/11/16.
 **/

public class FieldsPresenter implements IFieldsPresenter {

    private FieldsInteractor mInteractor;

    public FieldsPresenter() {
        mInteractor = new FieldsInteractor(this);
    }

    public void getSoccerFields(String pFacebookId, String pGroupId) {
        mInteractor.getFields(pFacebookId, pGroupId);
    }

    public void createSoccerField(String pFacebookId, SoccerField pSoccerField) {
        mInteractor.createField(pFacebookId, pSoccerField);
    }

    @Override
    public void onGetFieldsSuccessfully(List<SoccerField> pSoccerFields) {
        BusProvider.getBus().post(pSoccerFields);
    }

    @Override
    public void onGetFieldsFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }

    @Override
    public void onFieldCreatedSuccessfully(SoccerField pSoccerField) {
        BusProvider.getBus().post(pSoccerField);
    }

    @Override
    public void onFieldCreationFailed(ErrorMessage pErrorMessage) {
        BusProvider.getBus().post(pErrorMessage);
    }
}
