import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProductCategories } from './product-categories.model';
import { ProductCategoriesPopupService } from './product-categories-popup.service';
import { ProductCategoriesService } from './product-categories.service';

@Component({
    selector: 'jhi-product-categories-dialog',
    templateUrl: './product-categories-dialog.component.html'
})
export class ProductCategoriesDialogComponent implements OnInit {

    productCategories: ProductCategories;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private productCategoriesService: ProductCategoriesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.productCategories.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productCategoriesService.update(this.productCategories));
        } else {
            this.subscribeToSaveResponse(
                this.productCategoriesService.create(this.productCategories));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProductCategories>) {
        result.subscribe((res: ProductCategories) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ProductCategories) {
        this.eventManager.broadcast({ name: 'productCategoriesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-product-categories-popup',
    template: ''
})
export class ProductCategoriesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productCategoriesPopupService: ProductCategoriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.productCategoriesPopupService
                    .open(ProductCategoriesDialogComponent as Component, params['id']);
            } else {
                this.productCategoriesPopupService
                    .open(ProductCategoriesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
