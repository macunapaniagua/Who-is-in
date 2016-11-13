package com.soccer.whosin.fragments.members;

import com.google.gson.Gson;
import com.soccer.whosin.interfaces.IMembersPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.RetrofitHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class MembersInteractor {

    private IMembersPresenter mPresenter;

    public MembersInteractor(IMembersPresenter pPresenter) {
        this.mPresenter = pPresenter;
    }

    public void getMembersFromAPI(String pFacebookId, String pGroupId, boolean pAcceptedUsers) {
        WhoIsInService service = RetrofitHelper.getAPIWithHeaders(pFacebookId);
        service.getGroupMembers(pGroupId, pAcceptedUsers).enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if (response.isSuccessful())
                    mPresenter.onMembersRequestSuccessful(response.body());
                else {
                    ErrorMessage errorMessage;
                    try {
                        String error = response.errorBody().string();
                        errorMessage = new Gson().fromJson(error, ErrorMessage.class);
                    } catch (IOException e) {
                        errorMessage = new ErrorMessage(response.message());
                    }
                    mPresenter.onMembersRequestFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                ErrorMessage errorMessage = new ErrorMessage(t.getMessage());
                mPresenter.onMembersRequestFailed(errorMessage);
            }
        });
    }
}
