import { BaseEntity } from './../../shared';

export class Suppliers implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public mobileNo?: string,
        public location?: string,
        public email?: string,
        public products?: BaseEntity,
        public goodsReturned?: BaseEntity,
    ) {
    }
}
