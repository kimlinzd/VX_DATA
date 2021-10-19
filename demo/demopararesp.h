#ifndef DEMOPARARESP_H
#define DEMOPARARESP_H

#include "demoparabase.h"
#include "configstrunt.h"

class DemoParaResp : public DemoParaBase
{
    Q_OBJECT
public:
    explicit DemoParaResp(qreal sample = 62);
    virtual void getDemodata();        // 产生演示数据
    virtual void initDefSetInfo();     // 初始化设置

public slots:
    virtual void slot_SetInfo(qint32 paramod, qint32 cmd, const QByteArray &data); // 接收设置信息

private:
    RespSet m_demoRespSet;
    quint16 m_demoRespWave;  // 呼吸波形值
    qint32  m_demoRrValue;    // 呼吸率
    quint8  m_demoRespGain;    // 增益
    quint32 m_orderNums; // 取演示数据控制变量
    quint8 *m_buf;
    quint8 m_bufIndex;
};

#endif // DEMOPARARESP_H
