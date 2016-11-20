package com.soccer.whosin.interfaces;

import com.soccer.whosin.models.SoccerField;

/**
 * Created by Mario on 19/11/16.
 **/

public interface IFieldsInteractor {
    void getFields(String pFacebookId, String pGroupId);
    void createField(String pFacebookId, SoccerField pSoccerField);
}
