import { BaseEntity } from './../../shared';

export class SaleTransactions implements BaseEntity {
    constructor(
        public id?: number,
        public quantity?: number,
        public unitPrice?: number,
        public product?: BaseEntity,
        public sales?: BaseEntity,
    ) {
    }
}
