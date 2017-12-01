import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BrandsComponent } from './brands.component';
import { BrandsDetailComponent } from './brands-detail.component';
import { BrandsPopupComponent } from './brands-dialog.component';
import { BrandsDeletePopupComponent } from './brands-delete-dialog.component';

export const brandsRoute: Routes = [
    {
        path: 'brands',
        component: BrandsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brands'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'brands/:id',
        component: BrandsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brands'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const brandsPopupRoute: Routes = [
    {
        path: 'brands-new',
        component: BrandsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'brands/:id/edit',
        component: BrandsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'brands/:id/delete',
        component: BrandsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Brands'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
