import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProductCategoriesComponent } from './product-categories.component';
import { ProductCategoriesDetailComponent } from './product-categories-detail.component';
import { ProductCategoriesPopupComponent } from './product-categories-dialog.component';
import { ProductCategoriesDeletePopupComponent } from './product-categories-delete-dialog.component';

export const productCategoriesRoute: Routes = [
    {
        path: 'product-categories',
        component: ProductCategoriesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductCategories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'product-categories/:id',
        component: ProductCategoriesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductCategories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productCategoriesPopupRoute: Routes = [
    {
        path: 'product-categories-new',
        component: ProductCategoriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-categories/:id/edit',
        component: ProductCategoriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-categories/:id/delete',
        component: ProductCategoriesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProductCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
