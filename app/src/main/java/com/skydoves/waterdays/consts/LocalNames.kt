/*
 * Copyright (C) 2016 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.waterdays.consts

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object LocalNames {
  fun getLocalName(index: Int): String {
    when (index) {
      1 -> return "경기도"
      2 -> return "강원도"
      3 -> return "경상남도"
      4 -> return "경상북도"
      5 -> return "광주광역시"
      6 -> return "대구광역시"
      7 -> return "대전광역시"
      8 -> return "부산광역시"
      9 -> return "울산광역시"
      10 -> return "인천광역시"
      11 -> return "전라남도"
      12 -> return "전라북도"
      13 -> return "충청북도"
      14 -> return "충청남도"
      15 -> return "제주특별자치도"
      else -> return "서울특별시"
    }
  }
}
