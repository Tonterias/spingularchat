import { Component, OnInit, ElementRef } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IChatRoom, ChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from './chat-room.service';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';

@Component({
  selector: 'jhi-chat-room-update',
  templateUrl: './chat-room-update.component.html'
})
export class ChatRoomUpdateComponent implements OnInit {
  isSaving: boolean;

  chatusers: IChatUser[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    roomName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
    roomDescription: [null, [Validators.minLength(2), Validators.maxLength(250)]],
    privateRoom: [],
    image: [],
    imageContentType: [],
    chatUserId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected chatRoomService: ChatRoomService,
    protected chatUserService: ChatUserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatRoom }) => {
      this.updateForm(chatRoom);
    });
    this.chatUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatUser[]>) => response.body)
      )
      .subscribe((res: IChatUser[]) => (this.chatusers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(chatRoom: IChatRoom) {
    this.editForm.patchValue({
      id: chatRoom.id,
      creationDate: chatRoom.creationDate != null ? chatRoom.creationDate.format(DATE_TIME_FORMAT) : null,
      roomName: chatRoom.roomName,
      roomDescription: chatRoom.roomDescription,
      privateRoom: chatRoom.privateRoom,
      image: chatRoom.image,
      imageContentType: chatRoom.imageContentType,
      chatUserId: chatRoom.chatUserId
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatRoom = this.createFromForm();
    if (chatRoom.id !== undefined) {
      this.subscribeToSaveResponse(this.chatRoomService.update(chatRoom));
    } else {
      this.subscribeToSaveResponse(this.chatRoomService.create(chatRoom));
    }
  }

  private createFromForm(): IChatRoom {
    return {
      ...new ChatRoom(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      roomName: this.editForm.get(['roomName']).value,
      roomDescription: this.editForm.get(['roomDescription']).value,
      privateRoom: this.editForm.get(['privateRoom']).value,
      imageContentType: this.editForm.get(['imageContentType']).value,
      image: this.editForm.get(['image']).value,
      chatUserId: this.editForm.get(['chatUserId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatRoom>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackChatUserById(index: number, item: IChatUser) {
    return item.id;
  }
}
