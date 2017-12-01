import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SubCategories } from './sub-categories.model';
import { SubCategoriesPopupService } from './sub-categories-popup.service';
import { SubCategoriesService } from './sub-categories.service';
import { Products, ProductsService } from '../products';
import { ProductCategories, ProductCategoriesService } from '../product-categories';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sub-categories-dialog',
    templateUrl: './sub-categories-dialog.component.html'
})
export class SubCategoriesDialogComponent implements OnInit {

    subCategories: SubCategories;
    isSaving: boolean;

    products: Products[];

    productcategories: ProductCategories[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private subCategoriesService: SubCategoriesService,
        private productsService: ProductsService,
        private productCategoriesService: ProductCategoriesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productsService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.productCategoriesService.query()
            .subscribe((res: ResponseWrapper) => { this.productcategories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.subCategories.id !== undefined) {
            this.subscribeToSaveResponse(
                this.subCategoriesService.update(this.subCategories));
        } else {
            this.subscribeToSaveResponse(
                this.subCategoriesService.create(this.subCategories));
        }
    }

    private subscribeToSaveResponse(result: Observable<SubCategories>) {
        result.subscribe((res: SubCategories) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SubCategories) {
        this.eventManager.broadcast({ name: 'subCategoriesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductsById(index: number, item: Products) {
        return item.id;
    }

    trackProductCategoriesById(index: number, item: ProductCategories) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sub-categories-popup',
    template: ''
})
export class SubCategoriesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subCategoriesPopupService: SubCategoriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.subCategoriesPopupService
                    .open(SubCategoriesDialogComponent as Component, params['id']);
            } else {
                this.subCategoriesPopupService
                    .open(SubCategoriesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
