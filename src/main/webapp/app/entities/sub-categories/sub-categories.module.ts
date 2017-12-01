import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    SubCategoriesService,
    SubCategoriesPopupService,
    SubCategoriesComponent,
    SubCategoriesDetailComponent,
    SubCategoriesDialogComponent,
    SubCategoriesPopupComponent,
    SubCategoriesDeletePopupComponent,
    SubCategoriesDeleteDialogComponent,
    subCategoriesRoute,
    subCategoriesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...subCategoriesRoute,
    ...subCategoriesPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SubCategoriesComponent,
        SubCategoriesDetailComponent,
        SubCategoriesDialogComponent,
        SubCategoriesDeleteDialogComponent,
        SubCategoriesPopupComponent,
        SubCategoriesDeletePopupComponent,
    ],
    entryComponents: [
        SubCategoriesComponent,
        SubCategoriesDialogComponent,
        SubCategoriesPopupComponent,
        SubCategoriesDeleteDialogComponent,
        SubCategoriesDeletePopupComponent,
    ],
    providers: [
        SubCategoriesService,
        SubCategoriesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosSubCategoriesModule {}
