package com.skydoves.waterdays.consts

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object LocalUrls {
    fun getLocalUrl(index: Int): String {
        if (index == 1)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4182025000"
        else if (index == 2)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4282025000"
        else if (index == 3)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4817074000"
        else if (index == 4)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4729053000"
        else if (index == 5)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2920054000"
        else if (index == 6)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2720065000"
        else if (index == 7)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=3023052000"
        else if (index == 8)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2644058000"
        else if (index == 9)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=3114056000"
        else if (index == 10)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2871025000"
        else if (index == 11)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4681025000"
        else if (index == 12)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4579031000"
        else if (index == 13)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4376031000"
        else if (index == 14)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4376031000"
        else if (index == 15)
            return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=5013025300"
        return "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1159068000"
    }
}
