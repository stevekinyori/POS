import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    GoodsReceivedFromSupplierService,
    GoodsReceivedFromSupplierPopupService,
    GoodsReceivedFromSupplierComponent,
    GoodsReceivedFromSupplierDetailComponent,
    GoodsReceivedFromSupplierDialogComponent,
    GoodsReceivedFromSupplierPopupComponent,
    GoodsReceivedFromSupplierDeletePopupComponent,
    GoodsReceivedFromSupplierDeleteDialogComponent,
    goodsReceivedFromSupplierRoute,
    goodsReceivedFromSupplierPopupRoute,
} from './';

const ENTITY_STATES = [
    ...goodsReceivedFromSupplierRoute,
    ...goodsReceivedFromSupplierPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        GoodsReceivedFromSupplierComponent,
        GoodsReceivedFromSupplierDetailComponent,
        GoodsReceivedFromSupplierDialogComponent,
        GoodsReceivedFromSupplierDeleteDialogComponent,
        GoodsReceivedFromSupplierPopupComponent,
        GoodsReceivedFromSupplierDeletePopupComponent,
    ],
    entryComponents: [
        GoodsReceivedFromSupplierComponent,
        GoodsReceivedFromSupplierDialogComponent,
        GoodsReceivedFromSupplierPopupComponent,
        GoodsReceivedFromSupplierDeleteDialogComponent,
        GoodsReceivedFromSupplierDeletePopupComponent,
    ],
    providers: [
        GoodsReceivedFromSupplierService,
        GoodsReceivedFromSupplierPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosGoodsReceivedFromSupplierModule {}
