import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    GoodsReturnedToSupplierService,
    GoodsReturnedToSupplierPopupService,
    GoodsReturnedToSupplierComponent,
    GoodsReturnedToSupplierDetailComponent,
    GoodsReturnedToSupplierDialogComponent,
    GoodsReturnedToSupplierPopupComponent,
    GoodsReturnedToSupplierDeletePopupComponent,
    GoodsReturnedToSupplierDeleteDialogComponent,
    goodsReturnedToSupplierRoute,
    goodsReturnedToSupplierPopupRoute,
} from './';

const ENTITY_STATES = [
    ...goodsReturnedToSupplierRoute,
    ...goodsReturnedToSupplierPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        GoodsReturnedToSupplierComponent,
        GoodsReturnedToSupplierDetailComponent,
        GoodsReturnedToSupplierDialogComponent,
        GoodsReturnedToSupplierDeleteDialogComponent,
        GoodsReturnedToSupplierPopupComponent,
        GoodsReturnedToSupplierDeletePopupComponent,
    ],
    entryComponents: [
        GoodsReturnedToSupplierComponent,
        GoodsReturnedToSupplierDialogComponent,
        GoodsReturnedToSupplierPopupComponent,
        GoodsReturnedToSupplierDeleteDialogComponent,
        GoodsReturnedToSupplierDeletePopupComponent,
    ],
    providers: [
        GoodsReturnedToSupplierService,
        GoodsReturnedToSupplierPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosGoodsReturnedToSupplierModule {}
