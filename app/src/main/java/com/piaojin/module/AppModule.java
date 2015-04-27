package com.piaojin.module;

import android.content.Context;

import com.piaojin.common.UserInfo;
import com.piaojin.dao.MySqliteHelper;
import com.piaojin.helper.HttpHepler;
import com.piaojin.helper.MySharedPreferences;
import com.piaojin.helper.NetWorkHelper;
import com.piaojin.service.BackgroudService_;
import com.piaojin.ui.block.personalfile.PersonalFileActivity_;
import com.piaojin.ui.block.schedule.ScheduleActivity_;
import com.piaojin.ui.block.schedule.ScheduleFragment;
import com.piaojin.ui.block.schedule.ScheduleFragment_;
import com.piaojin.ui.block.task.TaskActivity_;
import com.piaojin.ui.block.task.TaskFragment;
import com.piaojin.ui.block.task.TaskFragment_;
import com.piaojin.ui.block.workmates.ContainerActivity_;
import com.piaojin.ui.block.workmates.WorkMateInfoFragment;
import com.piaojin.ui.block.workmates.WorkMateInfoFragment_;
import com.piaojin.ui.block.workmates.WorkMatesActivity_;
import com.piaojin.ui.block.workmates.chat.ChatFragment_;
import com.piaojin.ui.block.workmates.chat.LookFragment_;
import com.piaojin.ui.home.HomeFragment;
import com.piaojin.ui.home.HomeFragment_;
import com.piaojin.ui.message.MessageFragment;
import com.piaojin.ui.message.MessageFragment_;
import com.piaojin.ui.more.MoreFragment;
import com.piaojin.ui.more.MoreFragment_;
import com.piaojin.ui.sms.SmSFragment;
import com.piaojin.ui.sms.SmSFragment_;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import oa.piaojin.com.androidoa.HomeActivity_;
import oa.piaojin.com.androidoa.MainActivity_;

@Module(
        injects = {
                HomeActivity_.class, WorkMatesActivity_.class, HomeFragment_.class, MessageFragment_.class
                , SmSFragment_.class, MoreFragment_.class, WorkMateInfoFragment_.class, ContainerActivity_.class, LookFragment_.class
                , ChatFragment_.class, HttpHepler.class, MainActivity_.class, MySharedPreferences.class, NetWorkHelper.class, MySqliteHelper.class
                , BackgroudService_.class, ScheduleActivity_.class, ScheduleFragment_.class, UserInfo.class, MySqliteHelper.class, PersonalFileActivity_.class
                , TaskFragment_.class, TaskActivity_.class
        },
        complete = false,
        library = true
)
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    HomeFragment provideHomeFragment() {
        return HomeFragment_.builder().build();
    }

    @Provides
    @Singleton
    MessageFragment provideMessageFragment() {
        return MessageFragment_.builder().build();
    }

    @Provides
    @Singleton
    SmSFragment provideSmSFragment() {
        return SmSFragment_.builder().build();
    }

    @Provides
    @Singleton
    MoreFragment provideMoreFragment() {
        return MoreFragment_.builder().build();
    }

    @Provides
    @Singleton
    WorkMateInfoFragment provideWorkMateInfoFragment() {
        return WorkMateInfoFragment_.builder().build();
    }

    @Provides
    @Singleton
    HttpHepler provideHttpHepler() {
        return new HttpHepler();
    }

    @Provides
    @Singleton
    MySharedPreferences provideMySharedPreferences() {
        return new MySharedPreferences(mContext);
    }

    @Provides
    @Singleton
    NetWorkHelper provideNetWorkHelper() {
        return new NetWorkHelper();
    }

    @Provides
    @Singleton
    ScheduleFragment provideScheduleFragment() {
        return ScheduleFragment_.builder().build();
    }

    @Provides
    @Singleton
    TaskFragment provideTaskFragment() {
        return TaskFragment_.builder().build();
    }

    @Provides
    @Singleton
    UserInfo provideUserInfo() {
        return new UserInfo(mContext, new MySharedPreferences(mContext));
    }

    @Provides
    @Singleton
    MySqliteHelper provideMySqliteHelper() {
        return new MySqliteHelper(mContext);
    }
}
