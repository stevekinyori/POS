import { BaseEntity } from './../../shared';

export class SupplierProducts implements BaseEntity {
    constructor(
        public id?: number,
        public buyingPrice?: number,
        public standardBuyingPrice?: number,
        public retailPrice?: number,
        public deliveryLeadTime?: number,
        public firstItemDeliveryDate?: any,
        public minimumReorderLevel?: number,
        public maxReorderLevel?: number,
        public products?: BaseEntity,
        public supplier?: BaseEntity,
    ) {
    }
}
