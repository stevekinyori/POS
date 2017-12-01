import { BaseEntity } from './../../shared';

export class GoodsReceivedFromSupplier implements BaseEntity {
    constructor(
        public id?: number,
        public batchNo?: string,
        public quantity?: number,
        public unitPrice?: number,
        public pack?: BaseEntity,
        public batchProduct?: BaseEntity,
    ) {
    }
}
