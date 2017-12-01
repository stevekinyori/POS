import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    PackagingService,
    PackagingPopupService,
    PackagingComponent,
    PackagingDetailComponent,
    PackagingDialogComponent,
    PackagingPopupComponent,
    PackagingDeletePopupComponent,
    PackagingDeleteDialogComponent,
    packagingRoute,
    packagingPopupRoute,
} from './';

const ENTITY_STATES = [
    ...packagingRoute,
    ...packagingPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PackagingComponent,
        PackagingDetailComponent,
        PackagingDialogComponent,
        PackagingDeleteDialogComponent,
        PackagingPopupComponent,
        PackagingDeletePopupComponent,
    ],
    entryComponents: [
        PackagingComponent,
        PackagingDialogComponent,
        PackagingPopupComponent,
        PackagingDeleteDialogComponent,
        PackagingDeletePopupComponent,
    ],
    providers: [
        PackagingService,
        PackagingPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosPackagingModule {}
