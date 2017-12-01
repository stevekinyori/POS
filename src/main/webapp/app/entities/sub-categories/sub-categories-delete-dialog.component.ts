import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SubCategories } from './sub-categories.model';
import { SubCategoriesPopupService } from './sub-categories-popup.service';
import { SubCategoriesService } from './sub-categories.service';

@Component({
    selector: 'jhi-sub-categories-delete-dialog',
    templateUrl: './sub-categories-delete-dialog.component.html'
})
export class SubCategoriesDeleteDialogComponent {

    subCategories: SubCategories;

    constructor(
        private subCategoriesService: SubCategoriesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.subCategoriesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'subCategoriesListModification',
                content: 'Deleted an subCategories'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sub-categories-delete-popup',
    template: ''
})
export class SubCategoriesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subCategoriesPopupService: SubCategoriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.subCategoriesPopupService
                .open(SubCategoriesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
