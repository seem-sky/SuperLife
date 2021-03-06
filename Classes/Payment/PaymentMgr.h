//
//  PaymentMgr.h
//  SuperLife
//
//  Created by wang haibo on 15/1/29.
//
//

#ifndef __SuperLife__PaymentMgr__
#define __SuperLife__PaymentMgr__

#include "cocos2d.h"
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#import "IOSIAP.h"
#endif
typedef std::map<std::string, std::string> TProductInfo;
typedef std::vector<TProductInfo> TProductList;
typedef enum
{
    kPaySuccess = 0,
    kPayFail,
    kPayCancel,
    kPayTimeOut,
} PayResultCode;

class PayResultListener
{
public:
    virtual void onPayResult(PayResultCode ret, const char* msg, TProductInfo info) = 0;
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    virtual void onRequestProductsResult(ProductsRequestResult ret, TProductList info){}
#endif
};

class PaymentMgr : public cocos2d::Ref
{
    PaymentMgr();
    ~PaymentMgr();
public:
    // 单体
    static PaymentMgr* getInstance();
    
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    void setIAP(IOSIAP* iap);
#endif
    void payForProduct(TProductInfo info);
    void restorePurchase();
    
    void onPayResult(PayResultCode ret, const char* msg);
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    void onRequestProductsResult(ProductsRequestResult ret, TProductList info, const char* msg);
#endif
    const TProductList& getProductList() const;
    
    void setPayResultListener(PayResultListener* listener);
private:
    static bool     m_bPaying;
    TProductInfo    m_currentInfo;
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    IOSIAP*         m_pIAP;
#endif
    PayResultListener*  m_pPayResultListener;
    TProductList        m_ProductList;
};


#endif /* defined(__SuperLife__PaymentMgr__) */
