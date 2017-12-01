import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    SuppliersService,
    SuppliersPopupService,
    SuppliersComponent,
    SuppliersDetailComponent,
    SuppliersDialogComponent,
    SuppliersPopupComponent,
    SuppliersDeletePopupComponent,
    SuppliersDeleteDialogComponent,
    suppliersRoute,
    suppliersPopupRoute,
} from './';

const ENTITY_STATES = [
    ...suppliersRoute,
    ...suppliersPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SuppliersComponent,
        SuppliersDetailComponent,
        SuppliersDialogComponent,
        SuppliersDeleteDialogComponent,
        SuppliersPopupComponent,
        SuppliersDeletePopupComponent,
    ],
    entryComponents: [
        SuppliersComponent,
        SuppliersDialogComponent,
        SuppliersPopupComponent,
        SuppliersDeleteDialogComponent,
        SuppliersDeletePopupComponent,
    ],
    providers: [
        SuppliersService,
        SuppliersPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosSuppliersModule {}
