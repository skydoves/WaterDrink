package com.skydoves.waterdays.consts;

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class LocalNames {
    public static String getLocalName(int index) {
        if(index == 1)
            return "경기도";
        else if(index == 2)
            return "강원도";
        else if(index == 3)
            return "경상남도";
        else if(index == 4)
            return "경상북도";
        else if(index == 5)
            return "광주광역시";
        else if(index == 6)
            return "대구광역시";
        else if(index == 7)
            return "대전광역시";
        else if(index == 8)
            return "부산광역시";
        else if(index == 9)
            return "울산광역시";
        else if(index == 10)
            return "인천광역시";
        else if(index == 11)
            return "전라남도";
        else if(index == 12)
            return "전라북도";
        else if(index == 13)
            return "충청북도";
        else if(index == 14)
            return "충청남도";
        else if(index == 15)
            return "제주특별자치도";
        return "서울특별시";
    }
}