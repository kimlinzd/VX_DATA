#ifndef DEMOPARASPO2_H
#define DEMOPARASPO2_H

#include "demoparabase.h"

class DemoParaSpO2 : public DemoParaBase
{
    Q_OBJECT
public:
    explicit DemoParaSpO2(qreal sample = 60);
    virtual void getDemodata();        // 产生演示数据
    virtual void initDefSetInfo();     // 初始化设置

public slots:
    virtual void slot_SetInfo(qint32 paramod, qint32 cmd, const QByteArray &data); // 接收设置信息

private:
    void sendModuleInfo();    // 发送模块信息（模块查询回应）
    qint32 nSpo2Demo;
    qint32 nSpo2DemoBeat;
    qint32 nSpO2Wave;
    quint8 eLastSpo2Value;
    bool bSpo2Beat;
    quint8 eValuePulseBar;
    //-----------------//
    quint8 eWave;             // 波形值
    quint8 eState;            // 脉搏标记
    quint8 *m_buf;
    quint8 m_bufIndex;
};

#endif // DEMOPARASPO2_H
