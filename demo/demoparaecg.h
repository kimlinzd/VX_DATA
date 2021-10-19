#ifndef DEMOPARAECG_H
#define DEMOPARAECG_H

#include "demoparabase.h"
#include "configstrunt.h"

class DemoParaEcg : public DemoParaBase
{
    Q_OBJECT
public:
    explicit DemoParaEcg(qreal sample = 500);

    virtual void getDemodata();        // 产生演示数据
    virtual void initDefSetInfo();     // 初始化设置

public slots:
    virtual void slot_SetInfo(qint32 paramod, qint32 cmd, const QByteArray &data); // 接收设置信息

private:
    void sendEcgDataToPara();

private:
    EcgSet  m_demoEcgSet;
    qint32  m_nDemoEcgLoop, m_nDemoSecTimer, m_nDemoOneMvLoop; // 取波形数据控制变量
    quint8  m_ecgLead;    // 导联
    quint8  m_ecgCable;   // 5、3、6、12导联切换
    quint16 m_wEcgWave[8];
    bool    m_demoPace;     // 起博标志
    bool    m_demoBeat;
    bool    m_demo1mv;
    quint16  m_dataBaseLine;
    bool    isReapeat;
    quint8  m_canels;
    quint8 *m_EcgBuf;
    quint16 m_bufIndex;
};

#endif // DEMOPARAECG_H
