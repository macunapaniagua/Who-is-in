package com.soccer.whosin.members;

import com.soccer.whosin.interfaces.IMembersPresenter;
import com.soccer.whosin.interfaces.WhoIsInService;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.RetrofitHelper;

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

    public void getMembersFromAPI(String pGroupId) {
        WhoIsInService service = RetrofitHelper.getAPI();
//        service.getGroupMembers(pGroupId).enqueue(new Cal.....);
        service.getMembers().enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                mPresenter.onMembersRequestSuccessful(response.body());
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                mPresenter.onMembersRequestFailed(t.getMessage());
            }
        });
    }
}
