import { BaseEntity } from './../../shared';

export class ProductCategories implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public shtDescription?: string,
        public description?: string,
        public dateCreated?: any,
        public subCategories?: BaseEntity[],
    ) {
    }
}
