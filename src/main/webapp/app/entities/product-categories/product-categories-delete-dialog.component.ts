import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProductCategories } from './product-categories.model';
import { ProductCategoriesPopupService } from './product-categories-popup.service';
import { ProductCategoriesService } from './product-categories.service';

@Component({
    selector: 'jhi-product-categories-delete-dialog',
    templateUrl: './product-categories-delete-dialog.component.html'
})
export class ProductCategoriesDeleteDialogComponent {

    productCategories: ProductCategories;

    constructor(
        private productCategoriesService: ProductCategoriesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.productCategoriesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'productCategoriesListModification',
                content: 'Deleted an productCategories'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-product-categories-delete-popup',
    template: ''
})
export class ProductCategoriesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productCategoriesPopupService: ProductCategoriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.productCategoriesPopupService
                .open(ProductCategoriesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
