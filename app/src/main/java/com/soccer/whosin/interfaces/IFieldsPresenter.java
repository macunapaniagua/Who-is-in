package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.SoccerField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 19/11/16.
 **/

public interface IFieldsPresenter {
    void onGetFieldsSuccessfully(List<SoccerField> pSoccerFields);
    void onGetFieldsFailed(ErrorMessage pErrorMessage);
    void onFieldCreatedSuccessfully(SoccerField pSoccerField);
    void onFieldCreationFailed(ErrorMessage pErrorMessage);
}
