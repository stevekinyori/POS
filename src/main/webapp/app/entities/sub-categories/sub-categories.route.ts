import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SubCategoriesComponent } from './sub-categories.component';
import { SubCategoriesDetailComponent } from './sub-categories-detail.component';
import { SubCategoriesPopupComponent } from './sub-categories-dialog.component';
import { SubCategoriesDeletePopupComponent } from './sub-categories-delete-dialog.component';

export const subCategoriesRoute: Routes = [
    {
        path: 'sub-categories',
        component: SubCategoriesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubCategories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sub-categories/:id',
        component: SubCategoriesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubCategories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subCategoriesPopupRoute: Routes = [
    {
        path: 'sub-categories-new',
        component: SubCategoriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-categories/:id/edit',
        component: SubCategoriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-categories/:id/delete',
        component: SubCategoriesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SubCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
