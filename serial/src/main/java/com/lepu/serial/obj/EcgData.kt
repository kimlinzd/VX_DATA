package com.lepu.serial.obj

class EcgData {

    var len: Int // 数据长度

    /**
     * 导联数据， short数组
     */
    var I : ShortArray
    var II : ShortArray
    var III : ShortArray
    var aVR : ShortArray
    var aVL : ShortArray
    var aVF : ShortArray
    var V : ShortArray


    /**
     * 输入3通道数据
     */
    constructor(len: Int, I: ShortArray, II: ShortArray, V: ShortArray) {
        this.len = len
        this.I = I
        this.II = II
        this.V = V
        // III = II - I
        this.III = Array<Short>(500) { i ->  (II[i]- I[i]).toShort()}.toShortArray()
        // aVR = -(I+II)/2
        this.aVR = Array<Short>(500) { i ->  ((II[i]+ I[i])/-2).toShort()}.toShortArray()
        // aVL = (2*I-II)/2
        this.aVL = Array<Short>(500) { i ->  (I[i] - II[i]/2).toShort()}.toShortArray()
        // aVF = (2*II-I)/2
        this.aVF = Array<Short>(500) { i ->  (II[i] - I[i]/2).toShort()}.toShortArray()
        // v1
        this.V = Array<Short>(500) { i ->  (V[i]-0x8000).toShort()}.toShortArray()
    }

    /**
     * 输入7导联数据
     */
    constructor(
        len: Int,
        I: ShortArray,
        II: ShortArray,
        III : ShortArray,
        aVR : ShortArray,
        aVL : ShortArray,
        aVF : ShortArray,
        V: ShortArray
    ) {
        this.len = len
        this.I = I
        this.II = II
        this.III = III
        this.aVR = aVR
        this.aVL = aVL
        this.aVF = aVF
        this.V = V
    }

    override fun toString(): String {
        return """
            len: $len
        """.trimIndent()
    }
}