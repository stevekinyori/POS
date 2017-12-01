import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    SupplierProductsService,
    SupplierProductsPopupService,
    SupplierProductsComponent,
    SupplierProductsDetailComponent,
    SupplierProductsDialogComponent,
    SupplierProductsPopupComponent,
    SupplierProductsDeletePopupComponent,
    SupplierProductsDeleteDialogComponent,
    supplierProductsRoute,
    supplierProductsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...supplierProductsRoute,
    ...supplierProductsPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SupplierProductsComponent,
        SupplierProductsDetailComponent,
        SupplierProductsDialogComponent,
        SupplierProductsDeleteDialogComponent,
        SupplierProductsPopupComponent,
        SupplierProductsDeletePopupComponent,
    ],
    entryComponents: [
        SupplierProductsComponent,
        SupplierProductsDialogComponent,
        SupplierProductsPopupComponent,
        SupplierProductsDeleteDialogComponent,
        SupplierProductsDeletePopupComponent,
    ],
    providers: [
        SupplierProductsService,
        SupplierProductsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosSupplierProductsModule {}
