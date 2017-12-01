import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    ProductCategoriesService,
    ProductCategoriesPopupService,
    ProductCategoriesComponent,
    ProductCategoriesDetailComponent,
    ProductCategoriesDialogComponent,
    ProductCategoriesPopupComponent,
    ProductCategoriesDeletePopupComponent,
    ProductCategoriesDeleteDialogComponent,
    productCategoriesRoute,
    productCategoriesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...productCategoriesRoute,
    ...productCategoriesPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProductCategoriesComponent,
        ProductCategoriesDetailComponent,
        ProductCategoriesDialogComponent,
        ProductCategoriesDeleteDialogComponent,
        ProductCategoriesPopupComponent,
        ProductCategoriesDeletePopupComponent,
    ],
    entryComponents: [
        ProductCategoriesComponent,
        ProductCategoriesDialogComponent,
        ProductCategoriesPopupComponent,
        ProductCategoriesDeleteDialogComponent,
        ProductCategoriesDeletePopupComponent,
    ],
    providers: [
        ProductCategoriesService,
        ProductCategoriesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosProductCategoriesModule {}
