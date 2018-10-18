package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2016/10/24.
 */

public class MyFriendAndChecksBean {

    /**
     * state : 1
     * message : success
     * friendsArray : [{"userInfoId":17477,"rongUserId":"csco17477","userName":"程先平","imgUrl":"http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png"}]
     * checkArray : [{"userInfoId":17476,"rongUserId":"csco17476","userName":"刘静","imgUrl":"http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png"}]
     */

    private int state;
    private String message;
    /**
     * userInfoId : 17477
     * rongUserId : csco17477
     * userName : 程先平
     * imgUrl : http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png
     */

    private List<FriendsArrayBean> friendsArray;
    /**
     * userInfoId : 17476
     * rongUserId : csco17476
     * userName : 刘静
     * imgUrl : http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png
     */

    private List<CheckArrayBean> checkArray;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FriendsArrayBean> getFriendsArray() {
        return friendsArray;
    }

    public void setFriendsArray(List<FriendsArrayBean> friendsArray) {
        this.friendsArray = friendsArray;
    }

    public List<CheckArrayBean> getCheckArray() {
        return checkArray;
    }

    public void setCheckArray(List<CheckArrayBean> checkArray) {
        this.checkArray = checkArray;
    }

    public static class FriendsArrayBean {
        private int userInfoId;
        private String rongUserId;
        private String userName;
        private String imgUrl;
        private String pinyin;
        private String firstLetter;

        public FriendsArrayBean(int userInfoId, String rongUserId, String userName, String imgUrl, String pinyin, String firstLetter) {
            this.userInfoId = userInfoId;
            this.rongUserId = rongUserId;
            this.userName = userName;
            this.imgUrl = imgUrl;
            this.pinyin = pinyin;
            this.firstLetter = firstLetter;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public int getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(int userInfoId) {
            this.userInfoId = userInfoId;
        }

        public String getRongUserId() {
            return rongUserId;
        }

        public void setRongUserId(String rongUserId) {
            this.rongUserId = rongUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class CheckArrayBean {
        private int userInfoId;
        private String rongUserId;
        private String userName;
        private String imgUrl;
        private String pinyin;
        private String firstLetter;
        private int friendsUserType;

        public int getFriendsUserType() {
            return friendsUserType;
        }

        public void setFriendsUserType(int friendsUserType) {
            this.friendsUserType = friendsUserType;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public int getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(int userInfoId) {
            this.userInfoId = userInfoId;
        }

        public String getRongUserId() {
            return rongUserId;
        }

        public void setRongUserId(String rongUserId) {
            this.rongUserId = rongUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}