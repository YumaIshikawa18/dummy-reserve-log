package com.yuzukiku.dummy_reserve_log.domain.entity;

public enum status {
    RESERVED,             // 予約済み
    CANCELLED,            // キャンセル
    PENDING_CONFIRMATION, // 確認待ち
    PENDING_PAYMENT,      // 支払い待ち
    COMPLETED,            // 完了
    INVALID               // 無効
}