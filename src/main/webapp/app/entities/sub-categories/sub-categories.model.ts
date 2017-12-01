import { BaseEntity } from './../../shared';

export class SubCategories implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortDesciption?: string,
        public description?: string,
        public subCategoryProducts?: BaseEntity,
        public category?: BaseEntity,
    ) {
    }
}
