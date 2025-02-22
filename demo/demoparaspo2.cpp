﻿#include "demoparaspo2.h"
#include "appcommon.h"
#include "misc/log.h"
#include "misc/easybuff.h"
#include <QTime>
#include <QDebug>
#include <QByteArray>

#define SPO2_DEMO_BUF_COUNT 214

static quint8 eSpO2DemoWave[SPO2_DEMO_BUF_COUNT] = {
0x00,0x00,0x00,0x00,0x00,0x00,0x01,0x02,0x04,0x05,0x07,0x09,0x0c,0x0e,0x11,0x14,
0x17,0x1a,0x1d,0x20,0x23,0x26,0x29,0x2c,0x2f,0x32,0x34,0x36,0x39,0x3c,0x3f,0x42,
0x44,0x46,0x47,0x49,0x4c,0x4d,0x4f,0x50,0x51,0x53,0x53,0x54,0x55,0x56,0x56,0x56,
0x57,0x56,0x56,0x56,0x55,0x54,0x53,0x52,0x51,0x50,0x4f,0x4d,0x4c,0x4b,0x4a,0x49,
0x48,0x46,0x44,0x42,0x40,0x3e,0x3c,0x3a,0x38,0x36,0x35,0x33,0x31,0x2f,0x2d,0x2b,
0x29,0x28,0x27,0x26,0x25,0x24,0x23,0x22,0x21,0x20,0x1f,0x1e,0x1e,0x1e,0x1e,0x1d,
0x1d,0x1d,0x1d,0x1e,0x1e,0x1e,0x1f,0x1f,0x1f,0x20,0x20,0x21,0x21,0x21,0x22,0x22,
0x22,0x22,0x22,0x22,0x23,0x23,0x22,0x22,0x22,0x22,0x22,0x22,0x21,0x21,0x20,0x20,
0x1f,0x1f,0x1e,0x1e,0x1d,0x1d,0x1c,0x1c,0x1b,0x1b,0x1a,0x1a,0x19,0x19,0x18,0x18,
0x17,0x17,0x16,0x16,0x15,0x15,0x14,0x14,0x13,0x13,0x12,0x12,0x11,0x11,0x11,0x11,
0x10,0x10,0x0f,0x0f,0x0e,0x0e,0x0d,0x0d,0x0c,0x0c,0x0b,0x0b,0x0a,0x0a,0x0a,0x0a,
0x09,0x09,0x09,0x08,0x08,0x08,0x07,0x07,0x07,0x06,0x06,0x06,0x05,0x05,0x05,0x05,
0x04,0x04,0x04,0x03,0x03,0x03,0x02,0x02,0x02,0x02,0x01,0x01,0x01,0x01,0x01,0x00,
0x00,0x00,0x00,0x00,0x00,0x00
};

DemoParaSpO2::DemoParaSpO2(qreal sample) : DemoParaBase(sample)
{
    nSpo2Demo = 0;
    nSpo2DemoBeat = 0;
    eLastSpo2Value = 0;
    eValuePulseBar = 0;
    nSpO2Wave = 0;
    bSpo2Beat = false;
    m_buf = nullptr;
}

void DemoParaSpO2::getDemodata()
{
    if(m_buf == nullptr)
    {
        m_buf = EasyBuff::instance()->newBulkBuf_SpO2();
        m_bufIndex = 0;
    }

    quint32 nVal = eSpO2DemoWave[nSpo2Demo];
    nSpo2Demo = (nSpo2Demo+1) % SPO2_DEMO_BUF_COUNT;
    nVal += eSpO2DemoWave[nSpo2Demo];
    nSpo2Demo = (nSpo2Demo+1) % SPO2_DEMO_BUF_COUNT;
    nVal += eSpO2DemoWave[nSpo2Demo];
    nSpo2Demo = (nSpo2Demo+1) % SPO2_DEMO_BUF_COUNT;
    nVal += eSpO2DemoWave[nSpo2Demo];

    bSpo2Beat = nSpo2DemoBeat % 71;
    nSpo2DemoBeat = (nSpo2DemoBeat + 1) % SPO2_DEMO_BUF_COUNT;
    if(nSpo2DemoBeat == 0)
         nSpo2DemoBeat++;

    nVal >>= 2;

    if ( nSpO2Wave % 2 )
         nSpo2Demo = (nSpo2Demo + 1) % SPO2_DEMO_BUF_COUNT;

    eValuePulseBar = (quint8)nVal;

    if(eValuePulseBar > 100)
         eValuePulseBar = 100;

    eWave = eValuePulseBar;
    eState = ( bSpo2Beat? 0:0x01);
    eLastSpo2Value = eValuePulseBar;

    if ( nSpO2Wave >= 599)
        nSpO2Wave = 0;
    else
        nSpO2Wave++;
    //---------波形和状态-----------------//
    quint8* buf = m_buf + m_bufIndex * 2;
    *(buf++) = eWave;
    *buf = 0;
    m_bufIndex++;
    if(m_bufIndex >= BUFF_NUM_SPO2)
    {
        sig_ParaDataPtr(MSPO2, 1, m_buf);
        m_buf = nullptr;
        m_bufIndex = 0;
    }
    //-----------参数信息-----------------//
    if (eState > 0)   {
       qsrand(QTime(0,0,0).secsTo(QTime::currentTime()));
       quint8 eValueSpo2 = 94 + qrand() % 2;
       quint16 ValuePr = 60 + qrand()% 10;
       quint8 eValuePi = 55 + qrand()% 5;
       quint8 state = 0;
       quint8 ePrh = ValuePr >> 8;
       quint8 ePrl = ValuePr & 0x00FF;

       QByteArray paradata;
       paradata.append(eValueSpo2);
       paradata.append(ePrh);
       paradata.append(ePrl);
       paradata.append(char(0));
       paradata.append(eValuePi);
       paradata.append(char(0));
       paradata.append(char(0));
       paradata.append(state);

       emit sig_ParaData(MSPO2, 3, paradata);
    }
}

void DemoParaSpO2::initDefSetInfo()
{
    return;
}

void DemoParaSpO2::slot_SetInfo(qint32 paramod, qint32 cmd, const QByteArray &data)
{
    Q_UNUSED(data)

    if (paramod == MSPO2)
    {
        switch (cmd) {
        case 0x00:   //查询血氧模块信息
             sendModuleInfo();
            break;
        case 0x01:   //血氧模块数据包格式
            //LOG_OUT(DEMO, DEBUG, "SPO2 MODULE: %d; CONF: %d ", data[0], data[1]);
            logDebug(modParaSpo2, "SPO2 MODULE: %d; CONF: %d ", (int)data[0], (int)data[1]);
            break;
        default:
            break;
        }
    }
}

void DemoParaSpO2::sendModuleInfo()
{
    QByteArray info;
    quint8 module, conf;
    module = 0x00;
    conf = 0x17;
    QString pid = QString("SpO2_LFC_PM_Module_Demo_V1.0");
    info.append(module);
    info.append(conf);
    info.append(pid);
    emit sig_ParaData(MSPO2, 0x00, info);
}
