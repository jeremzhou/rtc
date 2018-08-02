/**
 * created on 2018年6月8日 下午1:59:59
 */
package cn.utstarcom.rtc.bo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
public final class CollectResult {

    private final AtomicInteger recivedNums;

    private final AtomicInteger errorNums;

    private final AtomicInteger discardNums;

    private final AtomicInteger fixNums;

    private final AtomicInteger handledNums;

    private final AtomicInteger sentNums;

    public CollectResult(AtomicInteger recivedNums, AtomicInteger errorNums,
            AtomicInteger discardNums, AtomicInteger fixNums, AtomicInteger handledNums,
            AtomicInteger sentNums) {
        super();
        this.recivedNums = recivedNums;
        this.errorNums = errorNums;
        this.discardNums = discardNums;
        this.handledNums = handledNums;
        this.fixNums = fixNums;
        this.sentNums = sentNums;
    }

    public AtomicInteger getRecivedNums() {
        return recivedNums;
    }

    public AtomicInteger getErrorNums() {
        return errorNums;
    }

    public AtomicInteger getDiscardNums() {
        return discardNums;
    }

    public AtomicInteger getFixNums() {
        return fixNums;
    }

    public AtomicInteger getHandledNums() {
        return handledNums;
    }

    public AtomicInteger getSentNums() {
        return sentNums;
    }

}
