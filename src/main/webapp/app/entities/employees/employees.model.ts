import { BaseEntity } from './../../shared';

export const enum GENDER {
    'MALE',
    'FEMALE'
}

export const enum Banks {
    'KCB',
    'EQUITY',
    'COOPERATIVE',
    'SAFARICOM',
    'OTHER'
}

export const enum PAYMENT_TYPES {
    'BANK',
    'MPESA',
    'CASH',
    'AIRTEL'
}

export class Employees implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public idNo?: number,
        public dob?: any,
        public age?: number,
        public gender?: GENDER,
        public estate?: string,
        public apartmentNo?: string,
        public email?: string,
        public bankName?: Banks,
        public accountNo?: string,
        public accountName?: string,
        public paymentMode?: PAYMENT_TYPES,
    ) {
    }
}
