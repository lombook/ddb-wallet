package com.jinglitong.wallet.server.service;


import com.jinglitong.wallet.api.model.logic.LBallanceVO;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.WalletVO;

public interface ChainService {
    public LWalletVO createWallet(WalletVO vo);

    public LBallanceVO getBallance(BalanceVO vo);

    public LWalletVO importWallet(WalletVO vo);

    public void payment(LPaymentVO vo, String chainType);

    public LBallanceVO getPayMents(BalanceVO vo);

    public LWalletVO modifyPasswd(WalletVO vo);

    public LWalletVO forgetPasswd(WalletVO vo);

    public LWalletVO exportWallet(WalletVO vo);

    public LBallanceVO getPasPrice(WalletVO vo);

    public void getTx(LPayResVO vo);
}
